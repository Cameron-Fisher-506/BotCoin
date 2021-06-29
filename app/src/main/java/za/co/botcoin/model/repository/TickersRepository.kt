package za.co.botcoin.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.ITickerDao
import za.co.botcoin.model.service.BotCoinService
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class TickersRepository(private val application: Application) {
    private val botCoinService: BotCoinService = BotCoinService()
    private val tickerDao: ITickerDao = BotCoinDatabase.getDatabase(application).tickerDao()

    private val updateLiveData by lazy { MutableLiveData<Boolean>() }

    fun getTickers(update: Boolean): LiveData<Resource<List<Ticker>>> {
        updateLiveData.value = update
        return Transformations.switchMap(updateLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { tickerDao.getMostRecentByPair() } },
                    { botCoinService.getTickers(GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)) },
                    {}
            )
        }
    }
}