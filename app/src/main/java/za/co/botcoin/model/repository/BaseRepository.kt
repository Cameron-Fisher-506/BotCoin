package za.co.botcoin.model.repository

import android.app.Activity
import za.co.botcoin.BotCoinApplication
import za.co.botcoin.model.service.BotCoinService
import za.co.botcoin.view.BaseActivity

abstract class BaseRepository {
    protected val botCoinService: BotCoinService = BotCoinService()
    private val baseActivity: Activity
        get() = BotCoinApplication().getInstance().topMostActivity

    init {
        (baseActivity as BaseActivity).displayProgressBar()
    }
}