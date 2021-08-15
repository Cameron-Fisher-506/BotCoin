package za.co.botcoin.services

import android.app.*
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
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.Trade
import za.co.botcoin.model.models.TradePrice
import za.co.botcoin.model.repository.AccountRepository
import za.co.botcoin.model.repository.WithdrawalRepository
import za.co.botcoin.utils.*
import za.co.botcoin.utils.GeneralUtils
import java.util.*
import kotlin.collections.ArrayList

class BotService : Service() {
    private lateinit var accountRepository: AccountRepository
    private lateinit var withdrawalRepository: WithdrawalRepository

    private var supportPrice: String = ""
    private var resistancePrice: String = ""
    private var supportPrices: ArrayList<TradePrice> = ArrayList()
    private var resistancePrices: ArrayList<TradePrice> = ArrayList()
    private var useTrailingStart: Boolean = false
    private val smartTrendDetectors: ArrayList<Double> = ArrayList()
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

        supportPrice = ""
        resistancePrice = ""
    }

    private fun attachTickersObserver() = CoroutineScope(Dispatchers.IO).launch {
        val resource = accountRepository.fetchTickers()
        when (resource.status) {
            Status.SUCCESS -> {
                val data = resource.data
                if (!data.isNullOrEmpty()) {
                    data.map { ticker ->
                        if (ticker.pair == ConstantUtils.PAIR_XRPZAR) {
                            marketTrend = getSmartTrendDetector(ticker.lastTrade.toDouble())
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
                    when {
                        lastTrade.type == Trade.BID_TYPE && marketTrend == Trend.UPWARD -> {
                            if (currentPrice > lastTrade.price.toDouble()) {
                                setResistancePrice(currentPrice, lastTrade)
                            }
                        }
                        lastTrade.type == Trade.ASK_TYPE && marketTrend == Trend.DOWNWARD -> setSupportPrice(currentPrice, lastTrade)
                    }
                } else {
                    lastTrade.type = Trade.ASK_TYPE
                    lastTrade.price = "0.0"
                    setSupportPrice(currentPrice, lastTrade)
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

                    if (useTrailingStart) {
                        val percentage = MathUtils.percentage(getLowestPrice(supportPrices), ConstantUtils.trailingStart)
                        val result = MathUtils.precision(getLowestPrice(supportPrices) + MathUtils.precision(percentage))
                        if (result < currentPrice) {
                            supportPrice = result.toString()
                            useTrailingStart = false
                        }
                    }

                    bid(true, currentPrice, lastTrade, zarBalance)
                    ask(true, currentPrice, lastTrade, xrpBalance, zarBalance)
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

                if (ConstantUtils.useTrailingStop) {
                    pullOutOfAsk(currentPrice, lastTrade, xrpBalance, zarBalance, lastAskOrder)
                    pullOutOfBid(currentPrice, lastTrade, xrpBalance, zarBalance, lastBidOrder)
                } else {
                    if (marketTrend == Trend.DOWNWARD) {
                        pullOutOfAsk(currentPrice, lastTrade, xrpBalance, zarBalance, lastAskOrder)
                        pullOutOfBid(currentPrice, lastTrade, xrpBalance, zarBalance, lastBidOrder)
                    }
                }
            }
            Status.ERROR -> {
            }
            Status.LOADING -> {
            }
        }
    }


    private fun attachStopOrderObserver(orderId: String, currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance, trailingStopPrice: Double = 0.0) = CoroutineScope(Dispatchers.IO).launch {
        val resource = withdrawalRepository.stopOrder(orderId)
        when (resource.status) {
            Status.SUCCESS -> {
                val data = resource.data
                if (!data.isNullOrEmpty()) {
                    if (data.first().success) {
                        GeneralUtils.notify(this@BotService, "Order Cancellation", "Order cancelled successfully.")

                        if (trailingStopPrice != 0.0) {
                            resistancePrice = trailingStopPrice.toString()
                            ask(false, currentPrice, lastTrade, xrpBalance, zarBalance)
                        }
                    } else {
                        GeneralUtils.notify(this@BotService, "Order Cancellation", "Order cancellation failed.")
                    }
                } else {
                    GeneralUtils.notify(this@BotService, "Order Cancellation", "Order cancellation failed.")
                }
            }
            Status.ERROR -> {
                GeneralUtils.notify(this@BotService, "Order Cancellation", "Order cancellation failed.")
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

    private fun bid(isRestrict: Boolean, currentPrice: Double, lastTrade: Trade, zarBalance: Balance) {
        if (isRestrict) {
            if (supportPrice.isNotBlank() && supportPrice != "0.0" && lastTrade.type != Trade.BID_TYPE && supportPrice.toDouble() < currentPrice) {
                Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - bid " +
                        "supportPrice: $supportPrice " +
                        "lastTradeType: ${lastTrade.type} " +
                        "currentPrice: $currentPrice " +
                        "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
                val amountXrpToBuy = calcAmountXrpToBuy(zarBalance.balance.toDouble(), supportPrice.toDouble()).toString()

                attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR, "BID", amountXrpToBuy, supportPrice)
                GeneralUtils.notify(this, "Auto Trade", "New buy order has been placed.")

                supportPrice = ""
                supportPrices.clear()
                resistancePrice = ""
                resistancePrices.clear()

                val supportPriceCounter = SharedPrefsUtils[applicationContext, SharedPrefsUtils.SUPPORT_PRICE_COUNTER]
                if (!supportPriceCounter.isNullOrBlank()) ConstantUtils.supportPriceCounter = supportPriceCounter.toInt() else ConstantUtils.supportPriceCounter = 4
            } else {
                Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - bid " +
                        "supportPrice: $supportPrice " +
                        "lastTradeType: ${lastTrade.type} " +
                        "currentPrice: $currentPrice " +
                        "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
            }
        } else {
            if (supportPrice.isNotBlank() && supportPrice != "0.0") {
                val percentage = MathUtils.percentage(supportPrice.toDouble(), ConstantUtils.trailingStop)
                val result = MathUtils.precision(supportPrice.toDouble() + MathUtils.precision(percentage))
                if (currentPrice >= result) {
                    GeneralUtils.notify(this, "bid isRestrict: false - (bid reset support: $supportPrice)", "$currentPrice >= $result")
                    supportPrice = ""
                }
            }
        }
    }

    private fun calcAmountXrpToBuy(zarBalance: Double, supportPrice: Double): Int = (zarBalance / supportPrice).toInt()

    private fun ask(isRestrict: Boolean, currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance) {
        var placeSellOrder = false
        var newSellPrice: String = ""
        var newResistancePrice: String = ""

        if (isRestrict) {
            if (resistancePrice.isNotBlank() && lastTrade.type == Trade.BID_TYPE && resistancePrice.toDouble() > lastTrade.price.toDouble() && resistancePrice.toDouble() > currentPrice) {
                val amountXrpToSell = (xrpBalance.balance.toDouble()).toInt().toString()
                attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell, resistancePrice)
                GeneralUtils.notify(this, "Auto Trade", "New buy order has been placed.")
                supportPrice = ""
                supportPrices.clear()
                resistancePrice = ""
                resistancePrices.clear()
            }
        } else {
            if (resistancePrice.isNotBlank() && resistancePrice != "0.0") {
                if (ConstantUtils.useTrailingStop) {
                    val percentage = MathUtils.percentage(resistancePrice.toDouble(), ConstantUtils.trailingStop)
                    val result = MathUtils.precision(resistancePrice.toDouble() - MathUtils.precision(percentage))
                    newResistancePrice = result.toString()
                    if (currentPrice <= result) {
                        newResistancePrice = result.toString()
                        placeSellOrder = true
                        useTrailingStart = true
                        GeneralUtils.notify(this, "ask - (ResistancePrice: $resistancePrice)", "$currentPrice <= $result")
                    }
                } else {
                    newResistancePrice = (resistancePrice.toDouble() + 0.1).toString()
                    if (currentPrice < newResistancePrice.toDouble()) {
                        placeSellOrder = true
                        GeneralUtils.notify(this, "ask - (ResistancePrice: $resistancePrice)", "$currentPrice <= $newResistancePrice")
                    }
                }
            } else if (lastTrade.price.toDouble() != 0.0 && lastTrade.type != Trade.ASK_TYPE) {
                if (ConstantUtils.useTrailingStop) {
                    val percentage = MathUtils.percentage(lastTrade.price.toDouble(), ConstantUtils.trailingStop)
                    val result = MathUtils.precision(lastTrade.price.toDouble() - MathUtils.precision(percentage))
                    newSellPrice = result.toString()
                    if (currentPrice <= result) {
                        newSellPrice = result.toString()
                        placeSellOrder = true
                        useTrailingStart = true
                        GeneralUtils.notify(this, "ask - (LastPurchasePrice: ${lastTrade.price.toDouble()})", "$currentPrice <= $result")
                    }
                } else {
                    newSellPrice = (lastTrade.price.toDouble() + 0.1).toString()
                    if (currentPrice < newSellPrice.toDouble()) {
                        placeSellOrder = true
                        GeneralUtils.notify(this, "ask - (LastPurchasePrice: ${lastTrade.price.toDouble()})", "$currentPrice <= $newSellPrice")
                    }
                }
            }
        }
        if (placeSellOrder) {
            val amountXrpToSell = (xrpBalance.balance.toDouble()).toInt().toString()
            when {
                newResistancePrice.isNotBlank() && newResistancePrice != "0.0" -> {
                    attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell, newResistancePrice)
                    GeneralUtils.notify(this, "Auto Trade", "New sell order has been placed.")

                    supportPrice = ""
                    supportPrices.clear()
                    resistancePrice = ""
                    resistancePrices.clear()
                }

                newSellPrice.isNotBlank() && newSellPrice != "0.0" -> {
                    attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell, newSellPrice)
                    GeneralUtils.notify(this, "Auto Trade", "New sell order has been placed.")

                    supportPrice = ""
                    supportPrices.clear()
                    resistancePrice = ""
                    resistancePrices.clear()
                }
                else -> {
                    Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - ask postOrder: null CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
                }
            }
        }
        Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - ask " +
                "resistancePrice: $resistancePrice " +
                "lastTradeType: ${lastTrade.type} " +
                "lastPurchasePrice: ${lastTrade.price} " +
                "currentPrice: $currentPrice " +
                "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
    }

    private fun getNumberOfPricesCounterMoreThanN(tradePrices: List<TradePrice>, lastTrade: Trade): Int {
        var toReturn = 0
        tradePrices.map {
            when (lastTrade.type) {
                "BID" -> if (it.counter > ConstantUtils.resistancePriceCounter) { toReturn++ }
                "ASK" -> if (!useTrailingStart) { if (it.counter > ConstantUtils.supportPriceCounter) { toReturn++ } }
            }
        }
        return toReturn
    }

    private fun getMaxCounter(tradePrices: ArrayList<TradePrice>): Int {
        var toReturn: Int = 0
        if (tradePrices.isNotEmpty()) {
            toReturn = tradePrices.first().counter
            tradePrices.map { if (toReturn < it.counter) { toReturn = it.counter } }
        }
        return toReturn
    }

    private fun getNumberOfPricesThatHaveCounter(tradePrices: ArrayList<TradePrice>, counter: Int): Int {
        var toReturn: Int = 0
        if (tradePrices.isNotEmpty()) {
            tradePrices.map { if (it.counter == counter) { toReturn++ } }
        }
        return toReturn
    }

    private fun getPriceEqualCounter(tradePrices: ArrayList<TradePrice>, counter: Int): Double {
        var toReturn: Double = 0.0
        if (tradePrices.isNotEmpty()) {
            tradePrices.map { if (it.counter == counter) { toReturn = it.price } }
        }
        return toReturn
    }

    private fun modifySupportPrices(supportPrices: ArrayList<TradePrice>, currentPrice: Double, lastTrade: Trade) {
        if (lastTrade.type == Trade.ASK_TYPE) {
            addPriceToList(supportPrices, currentPrice, false)
        }

        if (supportPrices.isNotEmpty()) {
            supportPrices.map {
                if (currentPrice > it.price) { it.isIncreased = true }
                if (currentPrice == it.price && it.isIncreased) {
                    it.counter++
                    it.isIncreased = false
                }
            }
        }
    }

    private fun modifyResistancePrices(resistancePrices: ArrayList<TradePrice>, currentPrice: Double, lastTrade: Trade) {
        if (lastTrade.type == Trade.BID_TYPE && currentPrice > lastTrade.price.toDouble()) {
            addPriceToList(resistancePrices, currentPrice, true)
        }

        if (resistancePrices.isNotEmpty()) {
            resistancePrices.map {
                if (currentPrice < it.price) { it.isIncreased = false }
                if (currentPrice == it.price && !it.isIncreased) {
                    it.counter++
                    it.isIncreased = true
                }
            }
        }
    }

    private fun addPriceToList(tradePrices: ArrayList<TradePrice>, currentPrice: Double, isIncreased: Boolean) {
        if (tradePrices.isNotEmpty()) { tradePrices.map { if (currentPrice == it.price) { return } } }
        tradePrices.add(TradePrice(currentPrice, isIncreased))
    }

    private fun getLowestPriceWithCounter(tradePrices: List<TradePrice>, maxCounter: Int): Double {
        var toReturn: Double = 0.0
        if (tradePrices.isNotEmpty()) {
            toReturn = tradePrices.first().price
            val prices = StringBuilder()
            tradePrices.map {
                prices.append("[${it.price}, ${it.counter}]")
                if (maxCounter == it.counter && toReturn > it.price) { toReturn = it.price }
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - getLowestPriceWithCounter " + "Prices: $prices" + "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
        }
        return toReturn
    }

    private fun getLowestPrice(tradePrices: List<TradePrice>): Double {
        var toReturn: Double = 0.0
        if (tradePrices.isNotEmpty()) {
            toReturn = tradePrices.first().price
            val prices = StringBuilder()
            tradePrices.map {
                prices.append("[${it.price}, ${it.counter}]")
                if (toReturn > it.price) { toReturn = it.price }
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - getLowestPrice " + "Prices: $prices" + "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
        }
        return toReturn
    }

    private fun getHighestPriceWithCounter(tradePrices: List<TradePrice>, maxCounter: Int): Double {
        var toReturn: Double = 0.0
        if (tradePrices.isNotEmpty()) {
            toReturn = tradePrices.first().price
            tradePrices.map {
                if (maxCounter == it.counter && toReturn < it.price) {
                    toReturn = it.price
                }
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - getHighestPriceWithCounter " + "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
        }
        return toReturn
    }

    private fun setSupportPrice(currentPrice: Double, lastTrade: Trade) {
        if (supportPrices.isNotEmpty()) {
            val prices = StringBuilder()
            supportPrices.map { prices.append("[${it.price}, ${it.counter}]") }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - setSupportPrice " + "SupportPrices: $prices " + "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
        }

        if (getNumberOfPricesCounterMoreThanN(supportPrices, lastTrade) == 1) {
            supportPrice = getPriceEqualCounter(supportPrices, getMaxCounter(supportPrices)).toString()
        } else if (getNumberOfPricesCounterMoreThanN(supportPrices, lastTrade) > 1) {
            if (getNumberOfPricesThatHaveCounter(supportPrices, getMaxCounter(supportPrices)) == 1) {
                supportPrice = getPriceEqualCounter(supportPrices, getMaxCounter(supportPrices)).toString()
            } else if (getNumberOfPricesThatHaveCounter(supportPrices, getMaxCounter(supportPrices)) > 1) {
                supportPrice = getLowestPriceWithCounter(supportPrices, getMaxCounter(supportPrices)).toString()
            }
        }
        modifySupportPrices(supportPrices, currentPrice, lastTrade)
    }

    private fun setResistancePrice(currentPrice: Double, lastTrade: Trade) {
        if (resistancePrices.isNotEmpty()) {
            val prices = StringBuilder()
            resistancePrices.map { prices.append("[${it.price}, ${it.counter}]") }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - setResistancePrice " + "ResistancePrices: $prices " + "CreatedTime: ${DateTimeUtils.getCurrentDateTime()}")
        }

        if (getNumberOfPricesCounterMoreThanN(resistancePrices, lastTrade) == 1) {
            resistancePrice = getPriceEqualCounter(resistancePrices, getMaxCounter(resistancePrices)).toString()
        } else if (getNumberOfPricesCounterMoreThanN(resistancePrices, lastTrade) > 1) {
            if (getNumberOfPricesThatHaveCounter(resistancePrices, getMaxCounter(resistancePrices)) == 1) {
                resistancePrice = getPriceEqualCounter(resistancePrices, getMaxCounter(resistancePrices)).toString()
            } else if (getNumberOfPricesThatHaveCounter(resistancePrices, getMaxCounter(resistancePrices)) > 1) {
                resistancePrice = getHighestPriceWithCounter(resistancePrices, getMaxCounter(resistancePrices)).toString()
            }
        }
        modifyResistancePrices(resistancePrices, currentPrice, lastTrade)
    }

    private fun pullOutOfAsk(currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance, lastAskOrder: Order) {
        if (lastAskOrder.limitPrice.isNotBlank()) {
            if (ConstantUtils.useTrailingStop) {
                val percentage = MathUtils.percentage(lastAskOrder.limitPrice.toDouble(), ConstantUtils.trailingStop)
                val result = MathUtils.precision(lastAskOrder.limitPrice.toDouble() - MathUtils.precision(percentage))
                if (currentPrice <= result) {
                    attachStopOrderObserver(lastAskOrder.id, currentPrice, lastTrade, xrpBalance, zarBalance, result)
                    GeneralUtils.notify(this, "pullOutOfAsk - (LastAskOrder: " + lastAskOrder.limitPrice + ")", "$currentPrice <= $result")
                }
            } else {
                if (lastAskOrder.createdTime.isNotBlank() &&
                    DateTimeUtils.differenceInMinutes(DateTimeUtils.convertLongToTime(lastAskOrder.createdTime.toLong()), DateTimeUtils.getCurrentDateTime()) > DateTimeUtils.FIVE_MINUTES) {
                    attachStopOrderObserver(lastAskOrder.id, currentPrice, lastTrade, xrpBalance, zarBalance, currentPrice + 0.1)
                    GeneralUtils.notify(this, "pullOutOfAsk - (LastAskOrder: " + lastAskOrder.limitPrice + ")", "$currentPrice <= ${currentPrice + 0.1}")
                }
            }
        } else {
            ask(false, currentPrice, lastTrade, xrpBalance, zarBalance)
        }
    }

    private fun pullOutOfBid(currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance, lastBidOrder: Order) {
        if (lastBidOrder.limitPrice.isNotBlank()) {
            if (ConstantUtils.useTrailingStop) {
                val percentage = MathUtils.percentage(lastBidOrder.limitPrice.toDouble(), ConstantUtils.trailingStop)
                val result = MathUtils.precision(lastBidOrder.limitPrice.toDouble() + MathUtils.precision(percentage))
                if (currentPrice >= result) {
                    attachStopOrderObserver(lastBidOrder.id, currentPrice, lastTrade, xrpBalance, zarBalance)
                    GeneralUtils.notify(this, "pullOutOfBidCancel - (LastBidOrder: " + lastBidOrder.limitPrice + ")", "$currentPrice >= $result")
                }
            } else {
                if (lastBidOrder.createdTime.isNotBlank() &&
                    DateTimeUtils.differenceInMinutes(DateTimeUtils.convertLongToTime(lastBidOrder.createdTime.toLong()), DateTimeUtils.getCurrentDateTime()) > DateTimeUtils.FIVE_MINUTES) {
                    attachStopOrderObserver(lastBidOrder.id, currentPrice, lastTrade, xrpBalance, zarBalance)
                    GeneralUtils.notify(this, "pullOutOfBidCancel - (LastBidOrder: " + lastBidOrder.limitPrice + ")", "$currentPrice >= ${lastBidOrder.limitPrice}")
                }
            }
        } else if (supportPrice.isNotBlank() && supportPrice != "0.0") {
            bid(false, currentPrice, lastTrade, zarBalance)
        }
    }

    private fun getSmartTrendDetector(currentPrice: Double): Trend {
        var toReturn: Trend = Trend.DOWNWARD

        if (this.smartTrendDetectors.size >= 720) {
            this.smartTrendDetectors.removeFirst()
            this.smartTrendDetectors.add(currentPrice)
        } else {
            this.smartTrendDetectors.add(currentPrice)
        }

        var maxCurrentPrice: Double = 0.0
        var useMaxCurrentPrice: Boolean = false
        if (this.smartTrendDetectors.isNotEmpty()) {
            for (i in 0 until this.smartTrendDetectors.size-1) {
                val smartTrendDetectorMargin = SharedPrefsUtils[applicationContext, SharedPrefsUtils.SMART_TREND_DETECTOR]
                if (!smartTrendDetectorMargin.isNullOrBlank()) ConstantUtils.smartTrendDetectorMargin = smartTrendDetectorMargin.toInt() else ConstantUtils.smartTrendDetectorMargin = 5

                if (!useMaxCurrentPrice) {
                    if (this.smartTrendDetectors[i+1] > this.smartTrendDetectors[i]) {
                        maxCurrentPrice = this.smartTrendDetectors[i+1]
                        toReturn = Trend.UPWARD
                    } else {
                        useMaxCurrentPrice = true
                        val percentage = MathUtils.percentage(this.smartTrendDetectors[i], ConstantUtils.smartTrendDetectorMargin)
                        val upperBounds = MathUtils.precision(this.smartTrendDetectors[i] + MathUtils.precision(percentage))
                        val lowerBounds = MathUtils.precision(this.smartTrendDetectors[i] - MathUtils.precision(percentage))

                        toReturn = if (this.smartTrendDetectors[i+1] in lowerBounds..upperBounds) {
                            Trend.UPWARD
                        } else {
                            Trend.DOWNWARD
                        }
                    }
                } else {
                    if (this.smartTrendDetectors[i+1] > maxCurrentPrice) {
                        useMaxCurrentPrice = false
                        maxCurrentPrice = this.smartTrendDetectors[i+1]
                        toReturn = Trend.UPWARD
                    } else {
                        val percentage = MathUtils.percentage(maxCurrentPrice, ConstantUtils.smartTrendDetectorMargin)
                        val upperBounds = MathUtils.precision(maxCurrentPrice + MathUtils.precision(percentage))
                        val lowerBounds = MathUtils.precision(maxCurrentPrice - MathUtils.precision(percentage))

                        toReturn = if (this.smartTrendDetectors[i+1] in lowerBounds..upperBounds) {
                            Trend.UPWARD
                        } else {
                            Trend.DOWNWARD
                        }
                    }
                }
            }
        }

        return toReturn
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        this.timer.cancel()
        this.timer.purge()
    }
}