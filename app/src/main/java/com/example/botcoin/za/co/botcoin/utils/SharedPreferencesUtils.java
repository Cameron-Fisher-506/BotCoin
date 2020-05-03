package com.example.botcoin.za.co.botcoin.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;


public class SharedPreferencesUtils {

    public static final String LUNO_API_PREF = "LUNO_API";
    public static final String AUTO_TRADE_PREF = "AUTO_TRADE";
    public static final String SUPPORT_RESISTANCE_PREF = "SUPPORT_RESISTANCE";

    public static void save(Context context, String sharedPrefName, JSONObject jsonObject){

        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(sharedPrefName, jsonObject.toString());
        editor.apply();
    }

    public static JSONObject get(Context context, String sharedPrefName)
    {
        JSONObject toReturn = null;

        try
        {
            SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, 0);
            String value = sharedPreferences.getString(sharedPrefName, "DEFAULT");

            if(value != null && !value.equals(""))
            {
                toReturn = new JSONObject(value);
            }
        }catch(Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: SharedPreferencesUtils - get"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

        return toReturn;
    }
}
