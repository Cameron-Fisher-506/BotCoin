package za.co.botcoin.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONObject


object SharedPrefsUtils {

    const val LAST_REQUEST_TIME: String = "LAST_REQUEST_TIME"

    fun save(context: Context, sharedPrefName: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(sharedPrefName, 0)
        val editor = sharedPreferences.edit()
        editor.putString(sharedPrefName, DateTimeUtils.getCurrentDateTime(DateTimeUtils.DASHED_PATTERN_YYYY_MM_DD_HH_MM_SS))
        editor.apply()
    }

    operator fun get(context: Context, sharedPrefName: String): String? {
        var toReturn: String? = null
        try {
            val sharedPreferences: SharedPreferences =
                    context.getSharedPreferences(sharedPrefName, 0)
            if (sharedPreferences != null && sharedPreferences.contains(sharedPrefName)) {
                val value = sharedPreferences.getString(sharedPrefName, "DEFAULT")
                if (value != null && value.isNotEmpty()) {
                    toReturn = value
                }
            }
        } catch (e: Exception) {
            Log.e("SharedPrefsUtils", """
                Error: ${e.message}
                Method: SharedPreferencesUtils - get
                CreatedTime: ${DateTimeUtils.getCurrentDateTime()}
                """.trimIndent()
            )
        }
        return toReturn
    }
}