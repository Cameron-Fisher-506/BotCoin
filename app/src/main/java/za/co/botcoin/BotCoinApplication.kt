package za.co.botcoin

import android.app.Application
import za.co.botcoin.view.BaseActivity

class BotCoinApplication : Application() {

    lateinit var topMostActivity: BaseActivity

    companion object {
        lateinit var instance: BotCoinApplication
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(BotCoinApplicationLifecycleObserver(this))
        instance = this
    }

}