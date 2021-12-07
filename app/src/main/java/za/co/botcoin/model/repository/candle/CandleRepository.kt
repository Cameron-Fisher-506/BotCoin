package za.co.botcoin.model.repository.candle

import android.app.Application
import za.co.botcoin.model.models.Candle
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.ICandleDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class CandleRepository(private val application: Application) : BaseRepository() {
    private val candleDao: ICandleDao = BotCoinDatabase.getDatabase(application).candleDao()


    suspend fun fetchCandles(pair: String, since: String, duration: Int): Resource<List<Candle>> {
        return DataAccessStrategyUtils.synchronizedCache(
            application,
            { BotCoinDatabase.getResource { candleDao.getAll() } },
            { botCoinService.getCandles("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", pair, since, duration) },
            { it.candles?.let { candles -> candleDao.upsert(candles, candleDao) } }
        )
    }
}