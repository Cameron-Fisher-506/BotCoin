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
import za.co.botcoin.state.Trend
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.models.Candle
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.Trade
import za.co.botcoin.model.repository.balance.BalanceRepository
import za.co.botcoin.model.repository.candle.CandleRepository
import za.co.botcoin.model.repository.order.OrderRepository
import za.co.botcoin.model.repository.postOrder.PostOrderRepository
import za.co.botcoin.model.repository.stopOrder.StopOrderRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.model.repository.trade.TradeRepository
import za.co.botcoin.state.ServiceState
import za.co.botcoin.utils.*
import za.co.botcoin.utils.DateTimeUtils.getPreviousMidnightUnixDateTime
import za.co.botcoin.utils.DateTimeUtils.isBeforeDateTime
import za.co.botcoin.utils.MathUtils.calculateMarginPercentage
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateConstant
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateGradient
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateX
import za.co.botcoin.utils.StraightLineFormulaUtils.calculateY
import java.util.*

class FiboService : Service() {
    private lateinit var tickersRepository: TickersRepository
    private lateinit var tradeRepository: TradeRepository
    private lateinit var balanceRepository: BalanceRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var stopOrderRepository: StopOrderRepository
    private lateinit var postOrderRepository: PostOrderRepository
    private lateinit var candleRepository: CandleRepository
    private val simpleMovingAverage: SimpleMovingAverage = SimpleMovingAverage(20)
    private lateinit var marketTrend: Trend
    private lateinit var lastCandle: Candle
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    companion object {
        private const val TICKER_RUN_TIME = 5000L
        private const val PAIR_XRPZAR = "XRPZAR"
        private const val BOTCOIN_TAG = "BOTCOIN"
        private const val XRP = "XRP"
        private const val ZAR = "ZAR"
    }

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
                Log.d(BOTCOIN_TAG, "AUTO TRADE RUNNING...")
            }
        }
        this.timer = Timer()
        this.timer.schedule(this.timerTask, 0, TICKER_RUN_TIME)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun init() {
        this.tickersRepository = TickersRepository(application)
        this.tradeRepository = TradeRepository(application)
        this.balanceRepository = BalanceRepository(application)
        this.orderRepository = OrderRepository(application)
        this.stopOrderRepository = StopOrderRepository(application)
        this.postOrderRepository = PostOrderRepository(application)
        this.candleRepository = CandleRepository(application)
    }

    private fun attachTickersObserver() = CoroutineScope(Dispatchers.IO).launch {
        val resource = tickersRepository.fetchTickers()
        when (resource.serviceState) {
            ServiceState.Success -> {
                val data = resource.data
                if (!data.isNullOrEmpty()) {
                    data.map { ticker ->
                        if (ticker.pair == PAIR_XRPZAR) {
                            attachTradesObserver(ticker.lastTrade.toDouble())
                        }
                    }
                }
            }
            ServiceState.Error -> {
            }
            ServiceState.Loading -> {
            }
        }
    }

    private fun attachTradesObserver(currentPrice: Double) = CoroutineScope(Dispatchers.IO).launch {
        val resource = tradeRepository.fetchTrades(PAIR_XRPZAR, true)
        when (resource.serviceState) {
            ServiceState.Success -> {
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
            ServiceState.Error -> {
            }
            ServiceState.Loading -> {
            }
        }
    }

    private fun attachBalancesObserver(currentPrice: Double, lastTrade: Trade) = CoroutineScope(Dispatchers.IO).launch {
        val resource = balanceRepository.fetchBalances()
        when (resource.serviceState) {
            ServiceState.Success -> {
                val data = resource.data
                var zarBalance: Balance = Balance()
                var xrpBalance: Balance = Balance()
                if (!data.isNullOrEmpty()) {
                    data.map { balance ->
                        if (balance.asset.equals(XRP, true)) {
                            xrpBalance = balance
                        } else if (balance.asset.equals(ZAR, true)) {
                            zarBalance = balance
                        }
                    }
                    if (::lastCandle.isInitialized) {
                        attachCandlesObserver(currentPrice, lastTrade, zarBalance, xrpBalance, PAIR_XRPZAR, lastCandle.timestamp)
                    } else {
                        attachCandlesObserver(currentPrice, lastTrade, zarBalance, xrpBalance, PAIR_XRPZAR)
                    }


                    if (::marketTrend.isInitialized && simpleMovingAverage.averages.isNotEmpty() && simpleMovingAverage.averages.size >= 20) {
                        Log.d("BOTCOIN", "Market trend: $marketTrend")
                        when {
                            marketTrend == Trend.UPWARD && simpleMovingAverage.isPriceOnLine(currentPrice) && lastTrade.type == Trade.BID_TYPE -> {
                                ask(true, currentPrice, lastTrade, xrpBalance, currentPrice+0.01)
                            }
                            marketTrend == Trend.DOWNWARD && simpleMovingAverage.isPriceOnLine(currentPrice) && lastTrade.type == Trade.ASK_TYPE -> {
                                bid(currentPrice, lastTrade, zarBalance, currentPrice-0.01)
                            }
                        }
                    }

                    attachOrdersObserver(currentPrice, lastTrade, xrpBalance, zarBalance)
                }
            }
            ServiceState.Error -> {
            }
            ServiceState.Loading -> {
            }
        }
    }

    private fun attachOrdersObserver(currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance) = CoroutineScope(Dispatchers.IO).launch {
        val response = orderRepository.fetchOrders()
        when (response.serviceState) {
            ServiceState.Success -> {
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
                trailingStop(currentPrice, lastTrade, xrpBalance, zarBalance, lastAskOrder)
            }
            ServiceState.Error -> {
            }
            ServiceState.Loading -> {
            }
        }
    }


    private fun attachStopOrderObserver(orderId: String, currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance) = CoroutineScope(Dispatchers.IO).launch {
        val resource = stopOrderRepository.stopOrder(orderId)
        when (resource.serviceState) {
            ServiceState.Success -> {
                val data = resource.data
                if (!data.isNullOrEmpty()) {

                } else {

                }
            }
            ServiceState.Error -> {
            }
            ServiceState.Loading -> {
            }
        }
    }

    private fun attachPostOrderObserver(pair: String, type: String, volume: String, price: String) = CoroutineScope(Dispatchers.IO).launch {
        val resource = postOrderRepository.postOrder(pair, type, volume, price)
        when (resource.serviceState) {
            ServiceState.Success -> {
                val data = resource.data
                if (!data.isNullOrEmpty()) {
                } else {
                }
            }
            ServiceState.Error -> {
            }
            ServiceState.Loading -> {
            }
        }
    }

    private fun attachCandlesObserver(currentPrice: Double, lastTrade: Trade, zarBalance: Balance, xrpBalance: Balance, pair: String, since: String = getPreviousMidnightUnixDateTime().toString(), duration: Int = 300) = CoroutineScope(Dispatchers.IO).launch {
        val resource = candleRepository.fetchCandles(pair, since, duration)
        when (resource.serviceState) {
            ServiceState.Success -> {
                val candles = resource.data?.reversed() ?: listOf()
                lastCandle = candles.last()
                val highestCandle = candles.maxByOrNull { candle -> candle.high }
                val lowestCandle = candles.minByOrNull { candle -> candle.low  }

                if (lowestCandle != null && highestCandle != null) {
                    marketTrend = if (isBeforeDateTime(DateTimeUtils.format(lowestCandle.timestamp.toLong()), DateTimeUtils.format(highestCandle.timestamp.toLong()))) {
                       val candlesSorted = candles.sortedBy { candle -> candle.low }

                        //trend line
                        var m = calculateGradient(candlesSorted.last().low.toDouble(), candlesSorted.first().low.toDouble(), candlesSorted.last().id.toDouble(), candlesSorted.first().id.toDouble())
                        var c = calculateConstant(candlesSorted.first().id.toDouble(), candlesSorted.first().low.toDouble(), m)
                        Log.d(BOTCOIN_TAG, "UPTREND - Bottom Line: " +
                                "lowestCandle: ${candlesSorted.first().low.toDouble()} " +
                                "highestCandle: ${candlesSorted.last().low.toDouble()} " +
                                "x2: ${candlesSorted.first().id.toDouble()} " +
                                "x1: ${candlesSorted.last().id.toDouble()} " +
                                "m: $m " +
                                "c: $c " +
                                "Point: ($currentPrice, ${candlesSorted.last().id.toDouble() + 1}) " +
                                "calculateX: ${calculateX(currentPrice, m, c)} " +
                                "calculateY: ${calculateY(candlesSorted.last().id.toDouble() + 1, m, c)} " +
                                "Trend: Upward" +
                                "lowest: ${lowestCandle.low} " +
                                "highest: ${highestCandle.high}"
                        )

                        m = calculateGradient(candlesSorted.last().close.toDouble(), candlesSorted.first().close.toDouble(), candlesSorted.last().id.toDouble(), candlesSorted.first().id.toDouble())
                        c = calculateConstant(candlesSorted.first().id.toDouble(), candlesSorted.first().close.toDouble(), m)
                        Log.d(BOTCOIN_TAG, "UPTREND - Top Line: " +
                                "lowestCandle: ${candlesSorted.first().close.toDouble()} " +
                                "highestCandle: ${candlesSorted.last().close.toDouble()} " +
                                "x2: ${candlesSorted.first().id.toDouble()} " +
                                "x1: ${candlesSorted.last().id.toDouble()} " +
                                "m: $m " +
                                "c: $c " +
                                "Point: ($currentPrice, ${candlesSorted.last().id.toDouble() + 2}) " +
                                "calculateX: ${calculateX(currentPrice, m, c)} " +
                                "calculateY: ${calculateY(candlesSorted.last().id.toDouble() + 2, m, c)} " +
                                "Trend: Upward " +
                                "lowest: ${lowestCandle.low} " +
                                "highest: ${highestCandle.high}"
                        )

                        Trend.UPWARD
                    } else {
                        val candlesSorted = candles.sortedByDescending { candle -> candle.high }

                        //trend line
                        var m = calculateGradient(candlesSorted[1].high.toDouble(), candlesSorted.first().high.toDouble(), candlesSorted[1].id.toDouble(), candlesSorted.first().id.toDouble() - 1)
                        var c = calculateConstant(candlesSorted.first().id.toDouble(), candlesSorted.first().high.toDouble(), m)
                        Log.d(BOTCOIN_TAG, "DOWNTREND - Top Line " +
                                "lowestCandle: ${candlesSorted[1].high.toDouble()} " +
                                "highestCandle: ${candlesSorted.first().high.toDouble()} " +
                                "x2: ${candlesSorted[1].id.toDouble()} " +
                                "x1: ${candlesSorted.first().id.toDouble()} " +
                                "m: $m " +
                                "c: $c " +
                                "Point: ($currentPrice, ${candlesSorted[1].id.toDouble() + 2}) " +
                                "calculateX: ${calculateX(currentPrice, m, c)} " +
                                "calculateY: ${calculateY(candlesSorted[1].id.toDouble() + 2, m, c)} " +
                                "Trend: Downward " +
                                "lowest: ${lowestCandle.low} " +
                                "highest: ${highestCandle.high}"
                        )

                        m = calculateGradient(candlesSorted[1].id.toDouble(), candlesSorted.first().id.toDouble(), candlesSorted[1].open.toDouble(), candlesSorted.first().open.toDouble())
                        c = calculateConstant(candlesSorted.first().id.toDouble(), candlesSorted.first().open.toDouble(), m)
                        Log.d(BOTCOIN_TAG, "DOWNTREND - Bottom Line " +
                                "lowestCandle: ${candlesSorted[1].open.toDouble()} " +
                                "highestCandle: ${candlesSorted.first().open.toDouble()} " +
                                "x2: ${candlesSorted[1].id.toDouble()} " +
                                "x1: ${candlesSorted.first().id.toDouble()} " +
                                "m: $m " +
                                "c: $c " +
                                "Point: ($currentPrice, ${candlesSorted[1].id.toDouble() + 1}) " +
                                "calculateX: ${calculateX(currentPrice, m, c)} " +
                                "calculateY: ${calculateY(candlesSorted[1].id.toDouble() + 1, m, c)} " +
                                "Trend: Downward " +
                                "lowest: ${lowestCandle.low} " +
                                "highest: ${highestCandle.high}"
                        )

                        Trend.DOWNWARD
                    }

                    val fiboRetracement = FibonacciRetracement()
                    fiboRetracement.calculateRetracements(highestCandle, lowestCandle, marketTrend)
                    Log.d(BOTCOIN_TAG, "fiboRetracement: " +
                            "HighestCandle: ${highestCandle.high}" +
                            "LowestCandle: ${lowestCandle.low}"
                    )
                    fiboRetracement.retracements.map {
                        Log.d(BOTCOIN_TAG, "retracement: $it")
                    }

                }

                simpleMovingAverage.calculateSma(resource.data?.reversed() ?: listOf())
            }
            ServiceState.Error -> {
            }
            ServiceState.Loading -> {
            }
        }
    }

    private fun bid(currentPrice: Double, lastTrade: Trade, zarBalance: Balance, supportPrice: Double) {
        if (supportPrice != 0.0 && lastTrade.type != Trade.BID_TYPE && supportPrice < currentPrice) {
            val amountXrpToBuy = calcAmountXrpToBuy(zarBalance.balance.toDouble(), supportPrice).toString()
            attachPostOrderObserver(PAIR_XRPZAR, "BID", amountXrpToBuy, supportPrice.toString())
            GeneralUtils.notify(this, "Auto Trade", "New buy order has been placed.")
        }
    }

    private fun calcAmountXrpToBuy(zarBalance: Double, supportPrice: Double): Int = (zarBalance / supportPrice).toInt()

    private fun ask(isRestrict: Boolean, currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, resistancePrice: Double = currentPrice+0.1) {
        var placeSellOrder = false
        if (isRestrict) {
            if (resistancePrice != 0.0 && lastTrade.type == Trade.BID_TYPE && resistancePrice > lastTrade.price.toDouble() && resistancePrice > currentPrice) {
                placeSellOrder = true
            }
        } else {
            if (lastTrade.price.toDouble() != 0.0 && lastTrade.type == Trade.BID_TYPE) {
                val result = calculateMarginPercentage(lastTrade.price.toDouble(), lastTrade.volume.toDouble(), ConstantUtils.trailingStop)
                if ((currentPrice * lastTrade.volume.toDouble()) <= result) {
                    placeSellOrder = true
                    GeneralUtils.notify(this, "ask - (LastPurchasePrice: ${lastTrade.price.toDouble()})", "${(currentPrice * lastTrade.volume.toDouble())} <= $result")
                }
            }
        }
        if (placeSellOrder) {
            val amountXrpToSell = (xrpBalance.balance.toDouble()).toInt().toString()
            attachPostOrderObserver(PAIR_XRPZAR, "ASK", amountXrpToSell, resistancePrice.toString())
            GeneralUtils.notify(this, "Auto Trade", "New sell order has been placed.")
        }
    }

    private fun trailingStop(currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance, lastAskOrder: Order) {
        if (lastAskOrder.limitPrice.isNotBlank()) {
            val result = calculateMarginPercentage(lastAskOrder.limitPrice.toDouble(), lastAskOrder.limitVolume.toDouble(), ConstantUtils.trailingStop)
            if ((currentPrice * lastAskOrder.limitVolume.toDouble())  <= result) {
                attachStopOrderObserver(lastAskOrder.id, currentPrice, lastTrade, xrpBalance, zarBalance)
                GeneralUtils.notify(this, "pullOutOfAsk - (LastAskOrder: " + lastAskOrder.limitPrice + ")", "${(currentPrice * lastAskOrder.limitVolume.toDouble())} <= $result")
            }
        } else {
            ask(false, currentPrice, lastTrade, xrpBalance)
        }
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()

        this.timer.cancel()
        this.timer.purge()
    }
}