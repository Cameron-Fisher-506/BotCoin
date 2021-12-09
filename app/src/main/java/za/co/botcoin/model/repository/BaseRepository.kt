package za.co.botcoin.model.repository

import android.app.Activity
import za.co.botcoin.BotCoinApplication
import za.co.botcoin.model.service.BotCoinService

abstract class BaseRepository {
    protected val botCoinService: BotCoinService = BotCoinService()
    private val baseActivity: Activity
        get() = BotCoinApplication().getInstance().topMostActivity

    protected fun showProgressDialog() {

    }
}