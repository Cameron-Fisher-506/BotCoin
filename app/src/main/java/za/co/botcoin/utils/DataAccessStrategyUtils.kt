package za.co.botcoin.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import za.co.botcoin.enum.Status

object DataAccessStrategyUtils {
    inline fun <A, T> lazyCache(crossinline dbQuery: () -> LiveData<Resource<T>>, crossinline wsCall: suspend () -> Resource<A>, crossinline saveCall: suspend (A) -> Unit) =
            liveData<Resource<T>>(Dispatchers.IO) {
                emit(Resource.loading())

                val result = dbQuery.invoke()
                emitSource(result)

                val response = wsCall.invoke()
                when (response.status) {
                    Status.SUCCESS -> {
                        response.data?.let { saveCall(it) }
                    }
                    Status.ERROR -> emit(Resource.error(response.message))
                    else -> emit(Resource.error("No data found."))
                }
            }

    inline fun <A, T> synchronizedCache(context: Context, crossinline dbQuery: suspend () -> Resource<T>, crossinline wsCall: suspend () -> Resource<A>, crossinline saveCall: suspend (A) -> Unit) =
            liveData<Resource<T>>(Dispatchers.IO) {
                emit(Resource.loading())

                val result = dbQuery.invoke()
                emit(result)

                val data = result.data
                if (data != null && (data as List<T>).size > 0) {
                    var mustUpdate = false
                    val oldDateTime = SharedPrefsUtils[context, SharedPrefsUtils.LAST_REQUEST_TIME]
                    if (oldDateTime != null) {
                        if (DateTimeUtils.differenceInMinutes(oldDateTime, DateTimeUtils.getCurrentDateTime()) > DateTimeUtils.ONE_MINUTE) {
                            mustUpdate = true
                        }
                    }

                    if (mustUpdate) {
                        val response = wsCall.invoke()
                        SharedPrefsUtils.save(context, SharedPrefsUtils.LAST_REQUEST_TIME)
                        when (response.status) {
                            Status.SUCCESS -> {
                                response.data?.let { saveCall(it) }

                                val result1 = dbQuery.invoke()
                                emit(result1)
                            }
                            Status.ERROR -> emit(Resource.error(response.message))
                            else -> emit(Resource.error("No data found."))
                        }
                    }
                } else {
                    val response = wsCall.invoke()
                    SharedPrefsUtils.save(context, SharedPrefsUtils.LAST_REQUEST_TIME)
                    when (response.status) {
                        Status.SUCCESS -> {
                            response.data?.let { saveCall(it) }

                            val result1 = dbQuery.invoke()
                            emit(result1)
                        }
                        Status.ERROR -> emit(Resource.error(response.message))
                        else -> emit(Resource.error("No data found."))
                    }
                }
            }
}