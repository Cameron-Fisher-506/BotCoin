package com.example.botcoin.za.co.botcoin.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.botcoin.za.co.botcoin.utils.ConstantUtils;
import com.example.botcoin.za.co.botcoin.utils.GeneralUtils;
import com.example.botcoin.za.co.botcoin.utils.StringUtils;
import com.example.botcoin.za.co.botcoin.utils.WSCallsUtils;

import org.json.JSONObject;

public class HttpGETTask extends AsyncTask<String, Void, JSONObject> {

    public static JSONObject get(String url)
    {
        JSONObject toReturn = null;

        try
        {
            HttpGETTask httpGETTask = new HttpGETTask();
            toReturn = httpGETTask.execute(url).get();
        }catch(Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: HttpGETTask - get"
                    + "\nURL: " + StringUtils.GLOBAL_LUNO_URL + "/api/1/balance"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }


        return toReturn;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... strings)
    {
        JSONObject toReturn = null;

        String url = strings[0];

        toReturn = WSCallsUtils.genericGET(StringUtils.GLOBAL_LUNO_URL + url);
        return toReturn;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject)
    {
        super.onPostExecute(jsonObject);
    }
}
