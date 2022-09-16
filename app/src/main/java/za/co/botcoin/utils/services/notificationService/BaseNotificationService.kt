package za.co.botcoin.utils.services.notificationService

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import za.co.botcoin.R

object BaseNotificationService {
    fun notify(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "BotCoin"
            notificationManager.createNotificationChannel(NotificationChannel(CHANNEL_ID, "BotCoin", NotificationManager.IMPORTANCE_DEFAULT))
            val notification = Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.botcoin)
                .setChannelId(CHANNEL_ID)
                .build()
            notificationManager.notify(0, notification)
        } else {
            val notification = Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.botcoin)
                .setContentIntent(PendingIntent.getActivity(context, 0, Intent(), 0))
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(0, notification)
        }
    }
}