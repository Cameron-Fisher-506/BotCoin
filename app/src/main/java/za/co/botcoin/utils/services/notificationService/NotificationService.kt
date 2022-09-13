package za.co.botcoin.utils.services.notificationService

import android.content.Context

class NotificationService(private val context: Context) : INotificationService {
    override fun notify(title: String, message: String) {
        BaseNotificationService.notify(context, title, message)
    }
}