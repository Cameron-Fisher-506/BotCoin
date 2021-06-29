package za.co.botcoin.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONObject
import za.co.botcoin.utils.GeneralUtils.getCurrentDateTime

object SharedPreferencesUtils {
    const val LUNO_API_PREF = "LUNO_API"
    const val AUTO_TRADE_PREF = "AUTO_TRADE"
    const val SUPPORT_RESISTANCE_PREF = "SUPPORT_RESISTANCE"
    const val PULLOUT_BID_PRICE_USER = "PULLOUT_BID_PRICE_USER"
    const val PULLOUT_BID_PRICE_DEFAULT = "PULLOUT_BID_PRICE_DEFAULT"
    const val PRIVACY_POLICY_ACCEPTANCE = "PRIVACY_POLICY_ACCEPTANCE"
    const val DISCLAIMER_ACCEPTANCE = "DISCLAIMER_ACCEPTANCE"
    const val SUPPORT_PRICE_COUNTER = "SUPPORT_PRICE_COUNTER"
    const val RESISTANCE_PRICE_COUNTER = "RESISTANCE_PRICE_COUNTER"
    const val LAST_REQUEST_TIME: String = "LAST_REQUEST_TIME"

    fun save(context: Context, sharedPrefName: String?, jsonObject: JSONObject) {
        val sharedPreferences = context.getSharedPreferences(sharedPrefName, 0)
        val editor = sharedPreferences.edit()
        editor.putString(sharedPrefName, jsonObject.toString())
        editor.apply()
    }

    @JvmStatic
    operator fun get(context: Context, sharedPrefName: String?): JSONObject? {
        var toReturn: JSONObject? = null
        try {
            val sharedPreferences = context.getSharedPreferences(sharedPrefName, 0)
            if (sharedPreferences != null && sharedPreferences.contains(sharedPrefName)) {
                val value = sharedPreferences.getString(sharedPrefName, "DEFAULT")
                if (value != null && value != "") {
                    toReturn = JSONObject(value)
                }
            }
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: SharedPreferencesUtils - get " +
                    "CreatedTime: ${getCurrentDateTime()}")
        }
        return toReturn
    }

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