package com.example.botcoin.za.co.botcoin.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GeneralUtils {
    public static String getCurrentDateTime()
    {
        String toReturn = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        now = calendar.getTime();

        toReturn = simpleDateFormat.format(now);

        return toReturn;

    }

    public static void makeToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getAuth()
    {
        String toReturn = null;

        try
        {
            String username = ConstantUtils.KEY_ID;
            String password = ConstantUtils.SECRET_KEY;

            String auth = username + ":" + password;
            byte[] authEncBytes = Base64.encode(auth.getBytes(), 0);
            toReturn = new String(authEncBytes);
        }catch(Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: getAuth"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }
        return toReturn;
    }

    public static boolean isApiCredentialsSaved(){
        boolean toReturn = false;

        if(ConstantUtils.KEY_ID != null && ConstantUtils.SECRET_KEY != null)
        {
            toReturn = true;
        }

        return toReturn;
    }

    public static AlertDialog createAlertDialog(Context context,String title,  String message, boolean isPrompt)
    {
        AlertDialog toReturn = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);


        if(isPrompt)
        {
            builder.setCancelable(true);
            builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

            builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        }else
        {
            builder.setCancelable(false);
            builder.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        }

        toReturn = builder.create();

        return toReturn;

    }
}
