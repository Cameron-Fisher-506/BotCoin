package com.example.botcoin.za.co.botcoin.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WSCallsUtils extends AsyncTask<String, Void, String>
{
    private WSCallUtilsCallBack wsCallUtilsCallBack;
    private int reqCode;

    public static String get(WSCallUtilsCallBack wsCallUtilsCallBack, int reqCode, String url, String auth)
    {
        String toReturn = null;

        try
        {
            WSCallsUtils wsCallsUtils = new WSCallsUtils(wsCallUtilsCallBack, reqCode);
            toReturn = wsCallsUtils.execute(ConstantUtils.REQUEST_METHOD_GET, url, auth).get();
        }catch(Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - get"
                    + "\nURL: " + url
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    private WSCallsUtils(WSCallUtilsCallBack wsCallUtilsCallBack, int reqCode)
    {
        this.wsCallUtilsCallBack = wsCallUtilsCallBack;
        this.reqCode = reqCode;
    }

    public static String post(WSCallUtilsCallBack wsCallUtilsCallBack, int reqCode, String url, String body, String auth)
    {
        String toReturn = null;

        try
        {
            WSCallsUtils wsCallsUtils = new WSCallsUtils(wsCallUtilsCallBack, reqCode);
            toReturn = wsCallsUtils.execute(ConstantUtils.REQUEST_METHOD_POST, url, body, auth).get();
        }catch(Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - post"
                    + "\nURL: " + url
                    + "\nBody: " + body
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        String toReturn = null;

        String requestMethod = strings[0];
        String url = strings[1];

        if(requestMethod.equals(ConstantUtils.REQUEST_METHOD_GET))
        {
            //GET
            String auth = strings[2];

            toReturn = genericGET(url, auth);
        }else if(requestMethod.equals(ConstantUtils.REQUEST_METHOD_POST))
        {
            //POST
            String body = strings[2];
            String auth = strings[3];

            toReturn = genericPOST(url, body, auth);
        }


        return toReturn;
    }


    @Override
    protected void onPostExecute(String response) {
        this.wsCallUtilsCallBack.taskCompleted(response, this.reqCode);
    }

    public String genericGET(String url, String auth)
    {
        String toReturn = null;

        HttpURLConnection connection = null;

        try
        {
            URL target = new URL(url);
            connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + auth);
            connection.setDoInput(true);

            connection.connect();

            if(connection.getResponseCode() >= 400)
            {
                //Cache Data

                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + connection.getResponseMessage()
                        + "\nCode: " + connection.getResponseCode()
                        + "\nURL: " + url
                        + "\nMethod: genericGET"
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
                toReturn = sb.toString();
            }
        }catch(Exception e)
        {
            //Cache Data

            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nURL: " + url
                    + "\nMethod: genericGET"
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

    public String genericPOST(String url, String body, String auth)
    {
        String toReturn = null;

        HttpURLConnection connection = null;

        try
        {
            URL target = new URL(url);
            connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + auth);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(body);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            connection.connect();
            if(connection.getResponseCode() >= 400)
            {
                //Cache Data

                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + connection.getResponseMessage()
                        + "\nCode: " + connection.getResponseCode()
                        + "\nURL: " + url
                        + "\nMethod: genericPOST"
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }else
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String output = "";

                while((output = bufferedReader.readLine()) != null) {
                    sb.append(output);
                }

                toReturn = sb.toString();
            }
        }catch(Exception e)
        {
            //Cache Data

            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nURL: " + url
                    + "\nMethod: genericPOST"
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
