package za.co.botcoin.model.repository.stopOrder

import android.app.Application
import za.co.botcoin.model.models.StopOrder
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IStopOrderDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource
import javax.inject.Inject

class StopOrderRepository @Inject constructor(private val application: Application) : BaseRepository() {
    private val stopOrderDao: IStopOrderDao = BotCoinDatabase.getDatabase(application).stopOrderDao()

    suspend fun stopOrder(orderId: String): Resource<List<StopOrder>> {
        return DataAccessStrategyUtils.synchronizedCache(
            application,
            { BotCoinDatabase.getResource { stopOrderDao.getAll() } },
            { botCoinService.stopOrder("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", orderId) },
            { stopOrderDao.upsert(it, stopOrderDao) }
        )
    }
}