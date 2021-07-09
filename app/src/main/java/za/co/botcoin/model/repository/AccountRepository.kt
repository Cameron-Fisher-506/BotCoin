package za.co.botcoin.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.botcoin.model.models.*
import za.co.botcoin.model.room.*
import za.co.botcoin.model.service.BotCoinService
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class AccountRepository(private val application: Application) {
    private val botCoinService: BotCoinService = BotCoinService()
    private val accountDao: IAccountDao = BotCoinDatabase.getDatabase(application).accountDao()
    private val tradeDao: ITradeDao = BotCoinDatabase.getDatabase(application).tradeDao()
    private val balanceDao: IBalanceDao = BotCoinDatabase.getDatabase(application).balanceDao()
    private val orderDao: IOrderDao = BotCoinDatabase.getDatabase(application).orderDao()
    private val postOrderDao: IPostOrderDao = BotCoinDatabase.getDatabase(application).postOrderDao()
    private val tickerDao: ITickerDao = BotCoinDatabase.getDatabase(application).tickerDao()

    init {
       CoroutineScope(Dispatchers.IO).launch {
           accountDao.insert(Account(1, "Luno Account"))
       }
    }

    suspend fun fetchTickers(): Resource<List<Ticker>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { tickerDao.getAll() } },
                { botCoinService.getTickers("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}") },
                { it.tickers?.let { tickers -> tickerDao.upsert(tickers, tickerDao) } }
        )
    }

    suspend fun fetchTrades(pair: String, sortDescending: Boolean) : Resource<List<Trade>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { tradeDao.getAllDesc() } },
                { botCoinService.getTrades("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", pair, sortDescending) },
                { it.trades?.let { trades -> tradeDao.upsert(trades, tradeDao) } }
        )
    }

    suspend fun fetchBalances(): Resource<List<Balance>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { balanceDao.getAll() } },
                { botCoinService.getBalances("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}") },
                { it.balance?.let { balance -> balanceDao.upsert(balance, balanceDao) } }
        )
    }

    suspend fun fetchOrders(): Resource<List<Order>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { orderDao.getAll() } },
                { botCoinService.getOrders("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}") },
                { it.orders?.let { orders -> orderDao.upsert(orders, orderDao) } }
        )
    }

    suspend fun postOrder(pair: String, type: String, volume: String, price: String): Resource<List<PostOrder>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { postOrderDao.getAll() } },
                { botCoinService.postOrder("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", pair, type, volume, price) },
                { postOrderDao.upsert(it, postOrderDao) }
        )
    }
}