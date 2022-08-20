package za.co.botcoin.model.repository.trade

import android.app.Application
import za.co.botcoin.model.models.Trade
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.ITradeDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class TradeRepository(private val application: Application) : BaseRepository() {
    private val tradeDao: ITradeDao = BotCoinDatabase.getDatabase(application).tradeDao()

    suspend fun fetchTrades(pair: String, sortDescending: Boolean): Resource<List<Trade>> {
        return DataAccessStrategyUtils.synchronizedCache(
            application,
            { BotCoinDatabase.getResource { tradeDao.getAllDesc() } },
            { botCoinService.getTrades("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", pair, sortDescending) },
            { it.trades?.let { trades -> tradeDao.upsert(trades, tradeDao) } }
        )
    }
}