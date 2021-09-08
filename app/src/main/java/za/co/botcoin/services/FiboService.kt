package za.co.botcoin.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.botcoin.R
import za.co.botcoin.enum.Status
import za.co.botcoin.enum.Trend
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.models.Candle
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.Trade
import za.co.botcoin.model.repository.AccountRepository
import za.co.botcoin.model.repository.WithdrawalRepository
import za.co.botcoin.utils.*
import java.util.*

class FiboService : Service() {
    private lateinit var accountRepository: AccountRepository
    private lateinit var withdrawalRepository: WithdrawalRepository
    private val simpleMovingAverage: SimpleMovingAverage = SimpleMovingAverage(20)
    private lateinit var marketTrend: Trend

    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "BotCoin"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "BotCoin",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
            val notification = Notification.Builder(applicationContext)
                .setContentTitle("BotCoin")
                .setContentText("BotCoin is auto trading!")
                .setSmallIcon(R.mipmap.botcoin)
                .setChannelId(CHANNEL_ID)
                .build()
            startForeground(1, notification)
        } else {
            GeneralUtils.notify(this, "BotCoin", "BotCoin is auto trading!")
        }

        init()

        this.timerTask = object : TimerTask() {
            override fun run() {
                attachTickersObserver()
                Log.d(ConstantUtils.BOTCOIN_TAG, "AUTO TRADE RUNNING...")
            }
        }
        this.timer = Timer()
        this.timer.schedule(this.timerTask, 0, ConstantUtils.TICKER_RUN_TIME)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun init() {
        this.accountRepository = AccountRepository(application)
        this.withdrawalRepository = WithdrawalRepository(application)
    }

    private fun attachTickersObserver() = CoroutineScope(Dispatchers.IO).launch {
        val resource = accountRepository.fetchTickers()
        when (resource.status) {
            Status.SUCCESS -> {
                val data = resource.data
                if (!data.isNullOrEmpty()) {
                    data.map { ticker ->
                        if (ticker.pair == ConstantUtils.PAIR_XRPZAR) {
                            attachTradesObserver(ticker.lastTrade.toDouble())
                        }
                    }
                }
            }
            Status.ERROR -> {
            }
            Status.LOADING -> {
            }
        }
    }

    private fun attachTradesObserver(currentPrice: Double) = CoroutineScope(Dispatchers.IO).launch {
        val resource = accountRepository.fetchTrades(ConstantUtils.PAIR_XRPZAR, true)
        when (resource.status) {
            Status.SUCCESS -> {
                val data = resource.data
                var lastTrade: Trade = Trade()
                if (!data.isNullOrEmpty()) {
                    lastTrade = data.first()
                } else {
                    lastTrade.type = Trade.ASK_TYPE
                    lastTrade.price = "0.0"
                }
                attachBalancesObserver(currentPrice, lastTrade)
            }
            Status.ERROR -> {
            }
            Status.LOADING -> {
            }
        }
    }

    private fun attachBalancesObserver(currentPrice: Double, lastTrade: Trade) = CoroutineScope(Dispatchers.IO).launch {
        val resource = accountRepository.fetchBalances()
        when (resource.status) {
            Status.SUCCESS -> {
                val data = resource.data
                var zarBalance: Balance = Balance()
                var xrpBalance: Balance = Balance()
                if (!data.isNullOrEmpty()) {
                    data.map { balance ->
                        if (balance.asset == ConstantUtils.XRP) {
                            xrpBalance = balance
                        } else if (balance.asset == ConstantUtils.ZAR) {
                            zarBalance = balance
                        }
                    }
                    if (simpleMovingAverage.dataSet.isNotEmpty()) {
                        val lastCandleDate = DateTimeUtils.convertLongToTime(simpleMovingAverage.dataSet.last().timestamp.toLong(), DateTimeUtils.DASHED_PATTERN_YYYY_MM_DD)
                        if (lastCandleDate != DateTimeUtils.getYesterdayDateTime(DateTimeUtils.DASHED_PATTERN_YYYY_MM_DD)) {
                            attachCandlesObserver(currentPrice, lastTrade, zarBalance, xrpBalance, ConstantUtils.PAIR_XRPZAR)
                        }
                    } else {
                        attachCandlesObserver(currentPrice, lastTrade, zarBalance, xrpBalance, ConstantUtils.PAIR_XRPZAR)
                    }

                    if (!::marketTrend.isInitialized) {
                        if (simpleMovingAverage.averages.isNotEmpty()) {
                            marketTrend = if (currentPrice > simpleMovingAverage.averages.last()) {
                                Trend.UPWARD
                            } else {
                                Trend.DOWNWARD
                            }
                        }
                    } else {
                        when {
                            marketTrend == Trend.UPWARD && simpleMovingAverage.isPriceOnLine(currentPrice) && lastTrade.type == Trade.BID_TYPE -> {
                                marketTrend = Trend.DOWNWARD
                                ask(currentPrice, lastTrade, xrpBalance, currentPrice+0.01)
                            }
                            marketTrend == Trend.DOWNWARD && simpleMovingAverage.isPriceOnLine(currentPrice) && lastTrade.type == Trade.ASK_TYPE -> {
                                marketTrend = Trend.UPWARD
                                bid(currentPrice, lastTrade, zarBalance, currentPrice-0.01)
                            }
                        }
                    }

                    attachOrdersObserver(currentPrice, lastTrade, xrpBalance, zarBalance)
                }
            }
            Status.ERROR -> {
            }
            Status.LOADING -> {
            }
        }
    }

    private fun attachOrdersObserver(currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance) = CoroutineScope(Dispatchers.IO).launch {
        val response = accountRepository.fetchOrders()
        when (response.status) {
            Status.SUCCESS -> {
                val data = response.data
                var lastAskOrder: Order = Order()
                var lastBidOrder: Order = Order()
                if (!data.isNullOrEmpty()) {
                    data.map { order ->
                        if (order.type == "ASK" && order.state == "PENDING") {
                            lastAskOrder = order
                        } else if (order.type == "BID" && order.state == "PENDING") {
                            lastBidOrder = order
                        }
                    }
                }

            }
            Status.ERROR -> {
            }
            Status.LOADING -> {
            }
        }
    }


    private fun attachStopOrderObserver(orderId: String, currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance) = CoroutineScope(Dispatchers.IO).launch {
        val resource = withdrawalRepository.stopOrder(orderId)
        when (resource.status) {
            Status.SUCCESS -> {
                val data = resource.data
                if (!data.isNullOrEmpty()) {

                } else {

                }
            }
            Status.ERROR -> {
            }
            Status.LOADING -> {
            }
        }
    }

    private fun attachPostOrderObserver(pair: String, type: String, volume: String, price: String) = CoroutineScope(Dispatchers.IO).launch {
        val resource = accountRepository.postOrder(pair, type, volume, price)
        when (resource.status) {
            Status.SUCCESS -> {
                val data = resource.data
                if (!data.isNullOrEmpty()) {
                } else {
                }
            }
            Status.ERROR -> {
            }
            Status.LOADING -> {
            }
        }
    }

    private fun attachCandlesObserver(currentPrice: Double, lastTrade: Trade, zarBalance: Balance, xrpBalance: Balance, pair: String, since: String = "1470810728478", duration: Int = 86400) = CoroutineScope(Dispatchers.IO).launch {
        val resource = accountRepository.fetchCandles(pair, since, duration)
        when (resource.status) {
            Status.SUCCESS -> {
                simpleMovingAverage.calculateSma(resource.data?.reversed() ?: listOf())
            }
            Status.ERROR -> {
            }
            Status.LOADING -> {
            }
        }
    }

    private fun bid(currentPrice: Double, lastTrade: Trade, zarBalance: Balance, supportPrice: Double) {
        if (supportPrice != 0.0 && lastTrade.type != Trade.BID_TYPE && supportPrice < currentPrice) {
            val amountXrpToBuy = calcAmountXrpToBuy(zarBalance.balance.toDouble(), supportPrice).toString()
            attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR, "BID", amountXrpToBuy, supportPrice.toString())
            GeneralUtils.notify(this, "Auto Trade", "New buy order has been placed.")
        }
    }

    private fun calcAmountXrpToBuy(zarBalance: Double, supportPrice: Double): Int = (zarBalance / supportPrice).toInt()

    private fun ask(currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, resistancePrice: Double) {
        if (resistancePrice != 0.0 && lastTrade.type == Trade.BID_TYPE && resistancePrice > lastTrade.price.toDouble() && resistancePrice > currentPrice) {
            val amountXrpToSell = (xrpBalance.balance.toDouble()).toInt().toString()
            attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell, resistancePrice.toString())
            GeneralUtils.notify(this, "Auto Trade", "New sell order has been placed.")
        }
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        this.timer.cancel()
        this.timer.purge()
    }
}