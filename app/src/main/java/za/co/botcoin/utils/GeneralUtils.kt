package za.co.botcoin.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import za.co.botcoin.services.BotService;

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

    public static String buildPostOrder(String pair, String type, String volume, String price)
    {
        String toReturn = null;

        if(price != null)
        {
            toReturn = "?"
                    + "pair=" + pair
                    + "&" + "type=" + type
                    + "&" + "volume=" + volume
                    + "&" + "price=" + price;
        }


        return toReturn;
    }

    public static String buildListTrades(String pair, boolean sortDesc)
    {
        String toReturn = null;

        toReturn = "?"
                + "pair=" + pair
                + "&" + "sort_desc=" + sortDesc;

        return toReturn;
    }

    public static String buildSend(String amount, String currency, String address, String tag)
    {
        String toReturn = null;

        if(tag != null)
        {
            toReturn = "?"
                    + "amount=" + amount
                    + "&" + "currency=" + currency
                    + "&" + "address=" + address
                    + "&" + "has_destination_tag=true"
                    + "&" + "destination_tag=" + tag;
        }else
        {
            toReturn = "?"
                    + "amount=" + amount
                    + "&" + "currency=" + currency
                    + "&" + "address=" + address;
        }


        return toReturn;
    }

    public static String buildWithdrawal(String amount, String beneficiaryId)
    {
        String toReturn = null;

        toReturn = "?"
                + "type=ZAR_EFT"
                + "&" + "amount=" + amount
                + "&" + "beneficiary_id=" + beneficiaryId;

        return toReturn;
    }

    public static String getAuth(String keyId, String secretKey)
    {
        String toReturn = null;

        try
        {
            String username = keyId;
            String password = secretKey;

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

    public static boolean isApiKeySet(Context context)
    {
        boolean toReturn = false;

        JSONObject jsonObjectLunoApiKey =  SharedPreferencesUtils.get(context, SharedPreferencesUtils.LUNO_API_PREF);
        if(jsonObjectLunoApiKey != null && jsonObjectLunoApiKey.has("keyID") && jsonObjectLunoApiKey.has("secretKey"))
        {
            try
            {
                ConstantUtils.USER_KEY_ID = jsonObjectLunoApiKey.getString("keyID");
                ConstantUtils.USER_SECRET_KEY = jsonObjectLunoApiKey.getString("secretKey");

                if(ConstantUtils.USER_SECRET_KEY != null && ConstantUtils.USER_KEY_ID != null)
                {
                    toReturn = true;
                }
            }catch(Exception e)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                        + "\nMethod: MainActivity - checkIfApiKeySet"
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }
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

    public static Bitmap createQRCode(String codeContent, int width, int height)
    {
        Bitmap toReturn = null;

        try
        {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(codeContent, BarcodeFormat.QR_CODE, width, height);
            toReturn = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                {
                    toReturn.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        }catch(Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: GeneralUtils - createBitmap"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    public static void runAutoTrade(Context context)
    {
        if(GeneralUtils.isApiKeySet(context))
        {
            JSONObject jsonObjectAutoTrade =  SharedPreferencesUtils.get(context, SharedPreferencesUtils.AUTO_TRADE_PREF);
            if(jsonObjectAutoTrade != null && jsonObjectAutoTrade.has("isAutoTrade"))
            {
                try
                {
                    boolean isAutoTrade = jsonObjectAutoTrade.getBoolean("isAutoTrade");

                    if(isAutoTrade)
                    {
                        if (Build.VERSION.SDK_INT >= 26) {
                            context.startForegroundService(new Intent(context, BotService.class));
                        }else
                        {
                            context.startService(new Intent(context, BotService.class));
                        }

                    }else
                    {

                        context.stopService(new Intent(context, BotService.class));
                    }

                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: MainActivity - runAutoTrade"
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
        }else
        {
            GeneralUtils.createAlertDialog(context,"Luno API Credentials","Please set your Luno API credentials in order to use BotCoin!", false).show();
            context.stopService(new Intent(context, BotService.class));
        }
    }

}
