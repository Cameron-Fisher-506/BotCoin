package za.co.botcoin.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.text.SimpleDateFormat
import java.util.*

object GeneralUtils {
    fun makeToast(context: Context?, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun getAuth(keyId: String, secretKey: String): String {
        try {
            val auth = "$keyId:$secretKey"
            val authEncBytes = Base64.encode(auth.toByteArray(), Base64.NO_WRAP)
            return String(authEncBytes)
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: getAuth " +
                    "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
        }
        return ""
    }

    fun isApiKeySet(context: Context?): Boolean {
        val lunoApiKeyId = context?.let { SharedPrefsUtils[it, SharedPrefsUtils.LUNO_API_KEY_ID] }
        val lunoApiSecretKey = context?.let { SharedPrefsUtils[it, SharedPrefsUtils.LUNO_API_SECRET_KEY] }
        if (!lunoApiKeyId.isNullOrBlank() && !lunoApiSecretKey.isNullOrBlank()) {
            ConstantUtils.USER_KEY_ID = lunoApiKeyId
            ConstantUtils.USER_SECRET_KEY = lunoApiSecretKey
            return true
        }
        return true
    }

    fun createAlertDialog(context: Context?, title: String, message: String, isPrompt: Boolean): AlertDialog {
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

        return builder.create()
    }

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
                    "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
        }
        return toReturn
    }
}