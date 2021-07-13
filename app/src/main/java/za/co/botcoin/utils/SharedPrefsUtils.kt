package za.co.botcoin.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPrefsUtils {
    const val LUNO_API_KEY_ID: String = "LUNO_API_KEY_ID"
    const val LUNO_API_SECRET_KEY: String = "LUNO_API_SECRET_KEY"
    const val AUTO_TRADE_PREF: String = "AUTO_TRADE"
    const val TRAILING_STOP: String = "TRAILING_STOP"
    const val PRIVACY_POLICY_ACCEPTANCE: String = "PRIVACY_POLICY_ACCEPTANCE"
    const val DISCLAIMER_ACCEPTANCE: String = "DISCLAIMER_ACCEPTANCE"
    const val RESISTANCE_PRICE_COUNTER: String = "RESISTANCE_PRICE_COUNTER"
    const val LAST_REQUEST_TIME: String = "LAST_REQUEST_TIME"
    const val SUPPORT_PRICE_COUNTER: String = "SUPPORT_PRICE_COUNTER"

    fun save(context: Context, key: String, value: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(key, 0)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    operator fun get(context: Context, sharedPrefName: String): String? {
        var toReturn: String? = null
        try {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(sharedPrefName, 0)
            if (sharedPreferences.contains(sharedPrefName)) {
                val value = sharedPreferences.getString(sharedPrefName, "DEFAULT")
                if (!value.isNullOrBlank()) {
                    toReturn = value
                }
            }
        } catch (e: Exception) {
            Log.e("SharedPrefsUtils", "Error: ${e.message} " +
                    "Method: SharedPreferencesUtils - get " +
                    "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
        }
        return toReturn
    }
}