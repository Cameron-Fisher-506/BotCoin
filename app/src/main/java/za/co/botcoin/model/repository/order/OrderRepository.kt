package za.co.botcoin.model.repository.order

import android.app.Application
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IOrderDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource
import javax.inject.Inject

class OrderRepository @Inject constructor(private val application: Application) : BaseRepository() {
    private val orderDao: IOrderDao = BotCoinDatabase.getDatabase(application).orderDao()

    suspend fun fetchOrders(): Resource<List<Order>> {
        return DataAccessStrategyUtils.synchronizedCache(
            application,
            { BotCoinDatabase.getResource { orderDao.getAll() } },
            { botCoinService.getOrders("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}") },
            { it.orders?.let { orders -> orderDao.upsert(orders, orderDao) } }
        )
    }
}