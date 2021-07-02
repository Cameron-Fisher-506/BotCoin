package za.co.botcoin.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.botcoin.model.models.Account
import za.co.botcoin.model.models.Trade
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IAccountDao
import za.co.botcoin.model.room.ITradeDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.model.service.BotCoinService
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class AccountRepository(private val application: Application) {
    private val botCoinService: BotCoinService = BotCoinService()
    private val accountDao: IAccountDao = BotCoinDatabase.getDatabase(application).accountDao()
    private val tradeDao: ITradeDao = BotCoinDatabase.getDatabase(application).tradeDao()

    private val updateLiveData by lazy { MutableLiveData<Boolean>() }

    init {
       CoroutineScope(Dispatchers.IO).launch {
           accountDao.insert(Account(1, "Luno Account"))
       }
    }

    fun fetchTrades(update: Boolean, pair: String, sortDescending: Boolean) : LiveData<Resource<List<Trade>>> {
        updateLiveData.value = update
        return Transformations.switchMap(updateLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { tradeDao.getAll() } },
                    { botCoinService.getTrades("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", pair, sortDescending) },
                    { it.trades?.let { trades -> tradeDao.upsert(trades, tradeDao) } }
            )
        }
    }

}