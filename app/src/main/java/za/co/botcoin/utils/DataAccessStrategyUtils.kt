package za.co.botcoin.utils

import android.content.Context
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.DateTimeUtils.ONE_MINUTE
import za.co.botcoin.utils.DateTimeUtils.getCurrentDateTime
import za.co.botcoin.utils.DateTimeUtils.getMinutesFrom

object DataAccessStrategyUtils {
    suspend inline fun <A, T> lazyCache(crossinline dbQuery: suspend () -> Resource<T>, crossinline wsCall: suspend () -> Resource<A>, crossinline saveCall: suspend (A) -> Unit): Resource<T> {
        val result = dbQuery.invoke()
        return if (result.data != null) {
            result
        } else {
            val response = wsCall.invoke()
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let { saveCall(it) }
                    dbQuery.invoke()
                }
                Status.ERROR -> Resource.error(response.message)
                else -> Resource.error("No data found.")
            }
        }
    }

    suspend inline fun <A, T> synchronizedCache(context: Context, crossinline dbQuery: suspend () -> Resource<T>, crossinline wsCall: suspend () -> Resource<A>, crossinline saveCall: suspend (A) -> Unit): Resource<T> {

        var toReturn = dbQuery.invoke()
        val data = toReturn.data
        if (data != null && (data as List<T>).size > 0) {
            var mustUpdate = true
            val oldDateTime = SharedPrefsUtils[context, SharedPrefsUtils.LAST_REQUEST_TIME]
            if (oldDateTime != null) {
                if (getMinutesFrom(oldDateTime, getCurrentDateTime()) > ONE_MINUTE) {
                    mustUpdate = true
                }
            }

            if (mustUpdate) {
                val response = wsCall.invoke()
                SharedPrefsUtils.save(context, SharedPrefsUtils.LAST_REQUEST_TIME, getCurrentDateTime())
                toReturn = when (response.status) {
                    Status.SUCCESS -> {
                        response.data?.let { saveCall(it) }
                        dbQuery.invoke()
                    }
                    Status.ERROR -> Resource.error(response.message)
                    else -> Resource.error("No data found.")
                }
            }
        } else {
            val response = wsCall.invoke()
            SharedPrefsUtils.save(context, SharedPrefsUtils.LAST_REQUEST_TIME, getCurrentDateTime())
            toReturn = when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let { saveCall(it) }
                    dbQuery.invoke()
                }
                Status.ERROR -> Resource.error(response.message)
                else -> Resource.error("No data found.")
            }
        }
        return toReturn
    }
}