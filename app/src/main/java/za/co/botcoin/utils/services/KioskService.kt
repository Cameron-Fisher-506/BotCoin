package za.co.botcoin.utils.services

import android.app.ActivityManager
import android.content.Context

object KioskService {
    fun isMyServiceRunning(context: Context, serviceClassName: String): Boolean {
        val manager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (service.service.className.contains(serviceClassName)) {
                return true
            }
        }
        return false
    }
}