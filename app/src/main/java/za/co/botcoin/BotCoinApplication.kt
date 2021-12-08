package za.co.botcoin

import android.app.Activity
import android.app.Application

class BotCoinApplication : Application() {

    lateinit var topMostActivity: Activity

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(BotCoinApplicationLifecycleObserver(this))
    }

    fun getInstance() : BotCoinApplication {
        return applicationContext.applicationContext as BotCoinApplication
    }
}