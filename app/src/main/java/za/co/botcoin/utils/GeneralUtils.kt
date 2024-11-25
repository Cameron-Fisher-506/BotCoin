package za.co.botcoin.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import za.co.botcoin.R
import za.co.botcoin.services.FiboService
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService

object GeneralUtils {
    private const val BOTCOIN_TAG = "BOTCOIN"

    fun getAuth(keyId: String, secretKey: String): String {
        try {
            val auth = "$keyId:$secretKey"
            val authEncBytes = Base64.encode(auth.toByteArray(), Base64.NO_WRAP)
            return String(authEncBytes)
        } catch (e: Exception) {
            Log.e(BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: getAuth " +
                    "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
        }
        return ""
    }

    fun isApiKeySet(context: Context?): Boolean {
        val lunoApiKeyId = context?.let { BaseSharedPreferencesService[it, BaseSharedPreferencesService.LUNO_API_KEY_ID] }
        val lunoApiSecretKey = context?.let { BaseSharedPreferencesService[it, BaseSharedPreferencesService.LUNO_API_SECRET_KEY] }
        if (!lunoApiKeyId.isNullOrBlank() && !lunoApiSecretKey.isNullOrBlank()) {
            ConstantUtils.USER_KEY_ID = lunoApiKeyId
            ConstantUtils.USER_SECRET_KEY = lunoApiSecretKey
            return true
        }
        return true
    }

    fun notify(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
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

    fun createQRCode(codeContent: String, width: Int, height: Int): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(codeContent, BarcodeFormat.QR_CODE, width, height)
        val toReturn: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                toReturn.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return toReturn
    }

    fun notify(context: Context?, title: String, message: String) {
        context?.let {
            val notificationManager = it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val CHANNEL_ID = "BotCoin"
                notificationManager.createNotificationChannel(NotificationChannel(CHANNEL_ID, "BotCoin", NotificationManager.IMPORTANCE_DEFAULT))
                val notification = Notification.Builder(it)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.mipmap.botcoin)
                        .setChannelId(CHANNEL_ID)
                        .build()
                notificationManager.notify(0, notification)
            } else {
                val notification = Notification.Builder(it)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.botcoin)
                        .setContentIntent(PendingIntent.getActivity(it, 0, Intent(), 0))
                        .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true)
                        .build()
                notificationManager.notify(0, notification)
            }
        }
    }

    fun runAutoTrade(context: Context) {
        if (isApiKeySet(context)) {
            val isAutoTrade = BaseSharedPreferencesService[context, BaseSharedPreferencesService.AUTO_TRADE_PREF]
            if (!isAutoTrade.isNullOrBlank() && isAutoTrade.toBoolean()) {
                if (Build.VERSION.SDK_INT >= 26) {
                    context.startForegroundService(Intent(context, FiboService::class.java))
                } else {
                    context.startService(Intent(context, FiboService::class.java))
                }
            } else {
                context.stopService(Intent(context, FiboService::class.java))
            }
        } else {
            context.stopService(Intent(context, FiboService::class.java))
        }
    }
}