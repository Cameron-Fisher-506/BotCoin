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

    private val fetchTickersLiveData by lazy { MutableLiveData<Boolean>() }
    private val fetchTradesLiveData by lazy { MutableLiveData<Boolean>() }
    private val fetchBalancesLiveData by lazy { MutableLiveData<Boolean>() }
    private val fetchOrdersLiveData by lazy { MutableLiveData<Boolean>() }
    private val postOrderLiveData by lazy { MutableLiveData<Boolean>() }

    init {
       CoroutineScope(Dispatchers.IO).launch {
           accountDao.insert(Account(1, "Luno Account"))
       }
    }

    fun fetchTickers(update: Boolean): LiveData<Resource<List<Ticker>>> {
        fetchTickersLiveData.value = update
        return Transformations.switchMap(fetchTickersLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { tickerDao.getAll() } },
                    { botCoinService.getTickers("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}") },
                    { it.tickers?.let { tickers -> tickerDao.upsert(tickers, tickerDao) } }
            )
        }
    }

    fun fetchTrades(update: Boolean, pair: String, sortDescending: Boolean) : LiveData<Resource<List<Trade>>> {
        fetchTradesLiveData.value = update
        return Transformations.switchMap(fetchTradesLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { tradeDao.getAllDesc() } },
                    { botCoinService.getTrades("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", pair, sortDescending) },
                    { it.trades?.let { trades -> tradeDao.upsert(trades, tradeDao) } }
            )
        }
    }

    fun fetchBalances(update: Boolean): LiveData<Resource<List<Balance>>> {
        fetchBalancesLiveData.value = update
        return Transformations.switchMap(fetchBalancesLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { balanceDao.getAll() } },
                    { botCoinService.getBalances("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}") },
                    { it.balance?.let { balance -> balanceDao.upsert(balance, balanceDao) } }
            )
        }
    }

    fun fetchOrders(mustFetchOrders: Boolean): LiveData<Resource<List<Order>>> {
        fetchOrdersLiveData.value = mustFetchOrders
        return Transformations.switchMap(fetchOrdersLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { orderDao.getAll() } },
                    { botCoinService.getOrders("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}") },
                    { it.orders?.let { orders -> orderDao.upsert(orders, orderDao) } }
            )
        }
    }

    fun postOrder(mustPostOrder: Boolean, pair: String, type: String, volume: String, price: String): LiveData<Resource<List<PostOrder>>> {
        postOrderLiveData.value = mustPostOrder
        return Transformations.switchMap(postOrderLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { postOrderDao.getAll() } },
                    { botCoinService.postOrder("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", pair, type, volume, price) },
                    { postOrderDao.upsert(it, postOrderDao) }
            )
        }
    }
}