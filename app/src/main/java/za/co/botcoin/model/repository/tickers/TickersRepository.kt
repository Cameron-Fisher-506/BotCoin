package za.co.botcoin.model.repository.tickers

import android.app.Application
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.ITickerDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource
import javax.inject.Inject

class TickersRepository @Inject constructor(private val application: Application) : BaseRepository() {
    var tickerDao: ITickerDao = BotCoinDatabase.getDatabase(application).tickerDao()

    suspend fun fetchTickers(): Resource<List<Ticker>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { tickerDao.getAll() } },
                { botCoinService.getTickers("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}") },
                { it.tickers?.let { tickers -> tickerDao.upsert(tickers, tickerDao) } }
        )
    }
}