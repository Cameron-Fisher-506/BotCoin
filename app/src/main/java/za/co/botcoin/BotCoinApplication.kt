package za.co.botcoin

import android.app.Application
import za.co.botcoin.di.ApplicationComponent
import za.co.botcoin.di.ApplicationModule
import za.co.botcoin.di.DaggerApplicationComponent
import za.co.botcoin.view.BaseActivity

class BotCoinApplication : Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

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