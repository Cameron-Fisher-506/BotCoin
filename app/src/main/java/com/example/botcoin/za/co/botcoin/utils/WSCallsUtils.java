package com.example.botcoin.za.co.botcoin.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class WSCallsUtils
{
    public static JSONObject genericGET(String url)
    {
        JSONObject toReturn = null;

        HttpURLConnection connection = null;

        try
        {
            URL target = new URL(url);
            connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + GeneralUtils.getAuth());
            connection.setDoInput(true);

            connection.connect();

            if(connection.getResponseCode() >= 400)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + connection.getResponseMessage()
                    + "\nCode: " + connection.getResponseCode()
                    + "\nURL: " + url
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }else
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output = "";
                StringBuilder sb = new StringBuilder();

                while((output = bufferedReader.readLine()) != null)
                {
                    sb.append(output);
                }
                Log.d(ConstantUtils.BOTCOIN_TAG, sb.toString());
                toReturn = new JSONObject(sb.toString());
            }
        }catch(Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nURL: " + url
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }finally
        {
            if(connection != null)
            {
                connection.disconnect();
                connection = null;
            }
        }

        return toReturn;
    }


}
