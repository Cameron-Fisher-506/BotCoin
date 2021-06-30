package za.co.botcoin.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import za.co.botcoin.services.BotService
import java.text.SimpleDateFormat
import java.util.*

object GeneralUtils {
    @JvmStatic
    fun getCurrentDateTime(): String {
        var toReturn = ""
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.ENGLISH)
        var now = Date()
        val calendar = Calendar.getInstance()
        calendar.time = now
        now = calendar.time
        toReturn = simpleDateFormat.format(now)
        return toReturn
    }

    fun makeToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    @JvmStatic
    fun buildPostOrder(pair: String, type: String, volume: String, price: String?): String? {
        var toReturn: String? = null
        if (price != null) {
            toReturn = ("?"
                    + "pair=" + pair
                    + "&" + "type=" + type
                    + "&" + "volume=" + volume
                    + "&" + "price=" + price)
        }
        return toReturn
    }

    @JvmStatic
    fun buildListTrades(pair: String, sortDesc: Boolean): String {
        var toReturn: String? = null
        toReturn = ("?"
                + "pair=" + pair
                + "&" + "sort_desc=" + sortDesc)
        return toReturn
    }

    @JvmStatic
    fun buildSend(amount: String, currency: String, address: String, tag: String?): String {
        return if (tag != null) {
            ("?"
                    + "amount=" + amount
                    + "&" + "currency=" + currency
                    + "&" + "address=" + address
                    + "&" + "has_destination_tag=true"
                    + "&" + "destination_tag=" + tag)
        } else {
            ("?"
                    + "amount=" + amount
                    + "&" + "currency=" + currency
                    + "&" + "address=" + address)
        }
    }

    @JvmStatic
    fun buildWithdrawal(amount: String, beneficiaryId: String): String {
        return  ("?"
                + "type=ZAR_EFT"
                + "&" + "amount=" + amount
                + "&" + "beneficiary_id=" + beneficiaryId)
    }

    @JvmStatic
    fun getAuth(keyId: String, secretKey: String): String {
        try {
            val auth = "$keyId:$secretKey"
            val authEncBytes = Base64.encode(auth.toByteArray(), Base64.NO_WRAP)
            return String(authEncBytes)
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: getAuth " +
                    "CreatedTime: ${getCurrentDateTime()}")
        }
        return ""
    }

    @JvmStatic
    fun isApiKeySet(context: Context?): Boolean {
        var toReturn = true
        val jsonObjectLunoApiKey = context?.let { SharedPreferencesUtils[it, SharedPreferencesUtils.LUNO_API_PREF] }
        if (jsonObjectLunoApiKey != null && jsonObjectLunoApiKey.has("keyID") && jsonObjectLunoApiKey.has("secretKey")) {
            try {
                ConstantUtils.USER_KEY_ID = jsonObjectLunoApiKey.getString("keyID")
                ConstantUtils.USER_SECRET_KEY = jsonObjectLunoApiKey.getString("secretKey")
                if (ConstantUtils.USER_SECRET_KEY != null && ConstantUtils.USER_KEY_ID != null) {
                    toReturn = true
                }
            } catch (e: Exception) {
                Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                        "Method: MainActivity - checkIfApiKeySet " +
                        "CreatedTime: ${getCurrentDateTime()}")
            }
        }
        return toReturn
    }

    @JvmStatic
    fun createAlertDialog(context: Context?, title: String?, message: String?, isPrompt: Boolean): AlertDialog? {
        var toReturn: AlertDialog? = null
        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)
        builder.setMessage(message)

        if (isPrompt) {
            builder.setCancelable(true)
            builder.setPositiveButton("Yes") { dialog, _ -> dialog.cancel() }
            builder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        } else {
            builder.setCancelable(false)
            builder.setPositiveButton("Okay") { dialog, _ -> dialog.cancel() }
        }
        toReturn = builder.create()

        return toReturn
    }

    @JvmStatic
    fun createQRCode(codeContent: String?, width: Int, height: Int): Bitmap? {
        var toReturn: Bitmap? = null
        try {
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix = qrCodeWriter.encode(codeContent, BarcodeFormat.QR_CODE, width, height)
            toReturn = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    toReturn.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: GeneralUtils - createBitmap " +
                    "CreatedTime: ${getCurrentDateTime()}")
        }
        return toReturn
    }

    fun runAutoTrade(context: Context) {
        /*if (isApiKeySet(context)) {
            val jsonObjectAutoTrade = SharedPreferencesUtils.get(context, SharedPreferencesUtils.AUTO_TRADE_PREF)
            if (jsonObjectAutoTrade != null && jsonObjectAutoTrade.has("isAutoTrade")) {
                try {
                    val isAutoTrade = jsonObjectAutoTrade.getBoolean("isAutoTrade")
                    if (isAutoTrade) {
                        if (Build.VERSION.SDK_INT >= 26) {
                            context.startForegroundService(Intent(context, BotService::class.java))
                        } else {
                            context.startService(Intent(context, BotService::class.java))
                        }
                    } else {
                        context.stopService(Intent(context, BotService::class.java))
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                            "Method: MainActivity - runAutoTrade " +
                            "CreatedTime: ${getCurrentDateTime()}")
                }
            }
        } else {
            createAlertDialog(context, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false)!!.show()
            context.stopService(Intent(context, BotService::class.java))
        }*/
    }
}