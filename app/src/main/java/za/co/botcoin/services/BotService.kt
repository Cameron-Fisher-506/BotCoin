package za.co.botcoin.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import org.json.JSONException
import za.co.botcoin.R
import za.co.botcoin.enum.Status
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.Trade
import za.co.botcoin.model.models.TradePrice
import za.co.botcoin.model.repository.AccountRepository
import za.co.botcoin.model.repository.WithdrawalRepository
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.MathUtils.percentage
import za.co.botcoin.utils.MathUtils.precision
import za.co.botcoin.utils.SharedPreferencesUtils
import java.util.*

class BotService : LifecycleService() {
    private lateinit var accountRepository: AccountRepository
    private lateinit var withdrawalRepository: WithdrawalRepository

    //Support/Resistance
    private var supportPrice: String? = null
    private var resistancePrice: String? = null

    //flags
    private var pullOutOfAskPrice: Double? = null

    private val handler: Handler = Handler()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "BotCoin"
            val channel = NotificationChannel(CHANNEL_ID,
                    "BotCoin",
                    NotificationManager.IMPORTANCE_DEFAULT)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
            val notification = Notification.Builder(applicationContext)
                    .setContentTitle("BotCoin")
                    .setContentText("BotCoin is auto trading!")
                    .setSmallIcon(R.mipmap.botcoin)
                    .setChannelId(CHANNEL_ID)
                    .build()
            startForeground(1, notification)
        } else {
            notify("BotCoin", "BotCoin is auto trading!")
        }

        //initialise values
        init()

        val delay: Long = ConstantUtils.TICKER_RUN_TIME
        handler.postDelayed(object : Runnable {
            override fun run() {
                //get current price
                attachTickersObserver()
                Log.d(ConstantUtils.BOTCOIN_TAG, "AUTO TRADE RUNNING...")
                handler.postDelayed(this, delay)
            }
        }, delay)
    }

    private fun attachTickersObserver() {
        accountRepository.fetchTickers(true).observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { ticker ->
                            if (ticker.pair == ConstantUtils.PAIR_XRPZAR) {
                                //getLastPurchase
                                attachTradesObserver(ticker.lastTrade.toDouble())
                            }
                        }
                    } else {

                    }
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        })
    }

    private fun attachTradesObserver(currentPrice: Double) {
        this.accountRepository.fetchTrades(true, ConstantUtils.PAIR_XRPZAR, true).observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    var lastTrade: Trade = Trade()
                    if (!data.isNullOrEmpty()) {
                        data.map { trade ->
                            lastTrade = trade
                            if (trade.type == Trade.BID_TYPE) {
                                if (currentPrice > lastTrade.price.toDouble()) {
                                    setResistancePrice(currentPrice, lastTrade)
                                }
                            } else {
                                setSupportPrice(currentPrice, lastTrade)
                            }
                        }
                    } else {
                        lastTrade.type = Trade.ASK_TYPE
                        lastTrade.price = "0.0"
                        //set support/resistance price
                        setSupportPrice(currentPrice, lastTrade)
                        //setResistancePrice();
                    }

                    //Get ZAR and XRP balance
                    attachBalancesObserver(currentPrice, lastTrade)
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        })
    }

    private fun attachBalancesObserver(currentPrice: Double, lastTrade: Trade) {
        this.accountRepository.fetchBalances(true).observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
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

                        //buy
                        bid(true, currentPrice, lastTrade, zarBalance)

                        //sell
                        ask(true, currentPrice, lastTrade, xrpBalance, zarBalance)

                        //get all the orders
                        attachOrdersObserver(currentPrice, lastTrade, xrpBalance, zarBalance)
                    } else {

                    }
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        })
    }

    private fun attachOrdersObserver(currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance) {
        this.accountRepository.fetchOrders(true).observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
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

                    //check if pull out is  necessary
                    pullOutOfAsk(currentPrice, lastTrade, xrpBalance, zarBalance, lastAskOrder)
                    pullOutOfBid(currentPrice, lastTrade, xrpBalance, zarBalance, lastBidOrder)
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        })
    }

    private fun attachStopOrderObserver(orderId: String, currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance) {
        this.withdrawalRepository.stopOrder(true, orderId).observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        if (data.first().success) {
                            notify("Order Cancellation", "Order cancelled successfully.")

                            val newResistancePrice = pullOutOfAskPrice
                            if (newResistancePrice != null) {
                                //place new sell order at trailing price
                                resistancePrice = newResistancePrice.toString()
                                ask(false,currentPrice, lastTrade, xrpBalance, zarBalance)
                            }
                            //lastAskOrder = null
                            //lastBidOrder = null
                            pullOutOfAskPrice = null
                        } else {
                            notify("Order Cancellation", "Order cancellation failed.")
                        }
                    } else {
                        notify("Order Cancellation", "Order cancellation failed.")
                    }

                }
                Status.ERROR -> {
                    notify("Order Cancellation", "Order cancellation failed.")
                }
                Status.LOADING -> {
                }
            }
        })
    }

    private fun attachReceiveObserver() {
        this.withdrawalRepository.receive(true, ConstantUtils.XRP).observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        /*val address_meta = jsonObject.getJSONArray("address_meta")
                        var address: String? = null
                        var tag: String? = null
                        if (data.first().address_meta != null && address_meta.length() > 0) {
                            for (i in 0 until address_meta.length()) {
                                val jsonObjectAddressMeta = address_meta.getJSONObject(i)
                                if (jsonObjectAddressMeta.getString("label") == "Address") {
                                    address = jsonObjectAddressMeta.getString("value")
                                }
                                if (jsonObjectAddressMeta.getString("label") == "XRP Tag") {
                                    tag = jsonObjectAddressMeta.getString("value")
                                }
                            }
                        }
                        if (address != null && tag != null) {
                            attachSendObserver(address, tag)
                        }*/
                    }
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        })
    }

    private fun attachPostOrderObserver(pair: String, type: String, volume: String, price: String) {
        this.accountRepository.postOrder(true, pair, type, volume, price).observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {

                    } else {

                    }
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        })
    }

    private fun attachSendObserver(amount: String, currency: String, address: String, destinationTag: String) {
        this.withdrawalRepository.send(true, amount, currency, address, destinationTag).observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { send -> if (send.success) notify("Sent $amount $currency to $address.", send.withdrawalId) else notify("Send failed.", "") }
                    } else {
                        notify("Send failed.", "")
                    }
                }
                Status.ERROR -> {
                    notify("Send failed.", "")
                }
                Status.LOADING -> {
                }
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    private fun init() {
        this.accountRepository = AccountRepository(application)
        this.withdrawalRepository = WithdrawalRepository(application)

        supportPrice = null
        resistancePrice = null
    }

    private fun bid(isRestrict: Boolean, currentPrice: Double, lastTrade: Trade, zarBalance: Balance) {
        val supportPriceTemp = supportPrice
        if (isRestrict) {
            if (supportPriceTemp != null && lastTrade.type != Trade.BID_TYPE && supportPriceTemp.toDouble() < currentPrice) {
                Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - bid " +
                        "supportPrice: $supportPrice " +
                        "lastTradeType: ${lastTrade.type} " +
                        "currentPrice: $currentPrice " +
                        "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                val amountXrpToBuy = calcAmountXrpToBuy(zarBalance.balance.toDouble(), supportPriceTemp.toDouble()).toString()

                attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR, "BID", amountXrpToBuy, supportPriceTemp)
                notify("Auto Trade", "New buy order has been placed.")
                //empty the the trade price list
                supportPrice = null
                ConstantUtils.supportPrices.clear()
                if (SharedPreferencesUtils[applicationContext, SharedPreferencesUtils.SUPPORT_PRICE_COUNTER] != null) {
                    try {
                        val jsonObjectSupportPriceCounter = SharedPreferencesUtils[applicationContext, SharedPreferencesUtils.SUPPORT_PRICE_COUNTER]
                        if (jsonObjectSupportPriceCounter != null && jsonObjectSupportPriceCounter.has(SharedPreferencesUtils.SUPPORT_PRICE_COUNTER)) {
                            ConstantUtils.supportPriceCounter = jsonObjectSupportPriceCounter.getInt(SharedPreferencesUtils.SUPPORT_PRICE_COUNTER)
                        }
                    } catch (e: JSONException) {
                        ConstantUtils.supportPriceCounter = 4
                    }
                } else {
                    ConstantUtils.supportPriceCounter = 4
                }
            } else {
                Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - bid " +
                        "supportPrice: $supportPrice " +
                        "lastTradeType: ${lastTrade.type} " +
                        "currentPrice: $currentPrice " +
                        "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
            }
        } else {
            if (supportPriceTemp != null) {
                val percentage = percentage(supportPriceTemp.toDouble(), ConstantUtils.trailingStop)
                if (percentage != null) {
                    val precision = precision(percentage)
                    if (precision != null) {
                        val result = precision(supportPriceTemp.toDouble() + precision)
                        if (result != null && currentPrice >= result) {
                            notify("bid isRestrict: false - (bid reset support: $supportPrice)", "$currentPrice >= $result")
                            supportPrice = null
                        }
                    }
                }
            }
        }
    }

    private fun calcAmountXrpToBuy(zarBalance: Double, supportPrice: Double): Int {
        var toReturn = 0
        toReturn = (zarBalance / supportPrice).toInt()
        return toReturn
    }

    private fun calcAmountXrpToSell(xrpBalance: Double): Int {
        var toReturn = 0
        toReturn = xrpBalance.toInt()
        return toReturn
    }

    private fun ask(isRestrict: Boolean, currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance) {
        var placeSellOrder = false
        var newSellPrice: String? = null

        val resistancePriceTemp = resistancePrice
        if (isRestrict) {
            if (resistancePriceTemp != null && lastTrade.type == Trade.BID_TYPE && resistancePriceTemp.toDouble() > lastTrade.price.toDouble() && resistancePriceTemp.toDouble() > currentPrice)
            {
                placeSellOrder = true
                Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - ask " +
                        "resistancePrice: $resistancePrice " +
                        "lastTradeType: $lastTrade.type " +
                        "lastPurchasePrice: ${lastTrade.price} " +
                        "currentPrice: $currentPrice " +
                        "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
            }
        } else {
            if (resistancePriceTemp != null) {
                val percentage = percentage(resistancePriceTemp.toDouble(), ConstantUtils.trailingStop)
                if (percentage != null) {
                    val precision = precision(percentage)
                    if (precision != null) {
                        val result = precision(resistancePriceTemp.toDouble() - precision)
                        if (result != null && currentPrice <= result) {
                            resistancePrice = result.toString()
                            placeSellOrder = true
                            notify("ask - (ResistancePrice: $resistancePrice)", "$currentPrice <= $result")
                            ConstantUtils.supportPriceCounter = 9
                        }
                    }
                }
            } else if (lastTrade.price.toDouble() != 0.0 && lastTrade.type != Trade.ASK_TYPE) {
                val percentage = percentage(lastTrade.price.toDouble(), ConstantUtils.trailingStop)
                if (percentage != null) {
                    val precision = precision(percentage)
                    if (precision != null) {
                        val result = precision(lastTrade.price.toDouble() - precision)
                        if (result != null && currentPrice <= result) {
                            newSellPrice = result.toString()
                            placeSellOrder = true
                            notify("ask - (LastPurchasePrice: ${lastTrade.price.toDouble()})", "$currentPrice <= $result")
                            ConstantUtils.supportPriceCounter = 9
                        }
                    }
                }
            }
        }
        if (placeSellOrder) {
            /*if (newXrpBalance >= ConstantUtils.SERVICE_FEE_MIN_BALANCE)
            {
                newXrpBalance -= 0.1;
                getBotCoinAccountDetails();
            }*/
            val amountXrpToSell = calcAmountXrpToSell(xrpBalance.balance.toDouble()).toString()
            var postOrder: String? = null

            val resistancePriceTemp = resistancePrice
            val newSellPriceTemp = newSellPrice
            when {
                resistancePriceTemp != null -> {
                    attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell, resistancePriceTemp)
                    notify("Auto Trade", "New sell order has been placed.")

                    //empty the the trade price list
                    resistancePrice = null
                    ConstantUtils.resistancePrices.clear()
                }

                newSellPriceTemp != null -> {
                    attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell, newSellPriceTemp)
                    notify("Auto Trade", "New sell order has been placed.")

                    //empty the the trade price list
                    resistancePrice = null
                    ConstantUtils.resistancePrices.clear()
                }
                else -> { Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - ask \npostOrder: null \nCreatedTime: ${GeneralUtils.getCurrentDateTime()}") }
            }
        }
        Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - ask " +
                "resistancePrice: $resistancePrice " +
                "lastTradeType: ${lastTrade.type} " +
                "lastPurchasePrice: ${lastTrade.price} " +
                "currentPrice: $currentPrice " +
                "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
    }

    private fun getNumberOfPricesCounterMoreThanTwo(tradePrices: List<TradePrice>, lastTrade: Trade): Int {
        var toReturn = 0
        for (i in tradePrices.indices) {
            if (lastTrade.type == "BID") {
                if (tradePrices[i].counter > ConstantUtils.resistancePriceCounter) {
                    toReturn++
                }
            } else if (lastTrade.type == "ASK") {
                if (tradePrices[i].counter > ConstantUtils.supportPriceCounter) {
                    toReturn++
                }
            }
        }
        return toReturn
    }

    private fun getMaxCounter(tradePrices: ArrayList<TradePrice>?): Int {
        var toReturn = 0
        if (tradePrices != null && tradePrices.isNotEmpty()) {
            toReturn = tradePrices[0].counter
            for (i in tradePrices.indices) {
                if (toReturn < tradePrices[i].counter) {
                    toReturn = tradePrices[i].counter
                }
            }
        }
        return toReturn
    }

    private fun getNumberOfPricesThatHaveCounter(tradePrices: ArrayList<TradePrice>?, counter: Int): Int {
        var toReturn = 0
        if (tradePrices != null && tradePrices.isNotEmpty()) {
            for (i in tradePrices.indices) {
                if (tradePrices[i].counter == counter) {
                    toReturn++
                }
            }
        }
        return toReturn
    }

    private fun getPriceEqualCounter(tradePrices: ArrayList<TradePrice>?, counter: Int): Double {
        var toReturn = 0.0
        if (tradePrices != null && tradePrices.isNotEmpty()) {
            for (i in tradePrices.indices) {
                if (tradePrices[i].counter == counter) {
                    toReturn = tradePrices[i].price
                }
            }
        }
        return toReturn
    }

    private fun modifySupportPrices(supportPrices: ArrayList<TradePrice>, currentPrice: Double) {
        addPriceToList(supportPrices, currentPrice, false)

        //check if the current price increases above the temp support price
        if (supportPrices.isNotEmpty()) {
            supportPrices.map {
                if (currentPrice > it.price) { it.isIncreased = true }
                if (currentPrice == it.price) {
                    if (it.isIncreased) {
                        it.counter++
                        it.isIncreased = false
                    }
                }
            }
        }
    }

    private fun modifyResistancePrices(resistancePrices: ArrayList<TradePrice>, currentPrice: Double) {
        addPriceToList(resistancePrices, currentPrice, true)

        //check if the current price increases above the temp support price
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
        if (tradePrices.isNotEmpty()) {
            tradePrices.map { if (currentPrice == it.price) { return } }

            //price is not in the list
            tradePrices.add(TradePrice(currentPrice, isIncreased))
        } else {
            tradePrices.add(TradePrice(currentPrice, isIncreased))
        }
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
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - getLowestPriceWithCounter " + "Prices: $prices" + "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
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
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - getHighestPriceWithCounter " + "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }
        return toReturn
    }

    private fun setSupportPrice(currentPrice: Double, lastTrade: Trade) {
        if (ConstantUtils.supportPrices.isNotEmpty()) {
            val prices = StringBuilder()
            ConstantUtils.supportPrices.map { prices.append("[${it.price}, ${it.counter}]") }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - setSupportPrice " + "SupportPrices: $prices " + "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }

        //Get the number of prices counter more than 2
        if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.supportPrices, lastTrade) == 1) {
            //Only 1 price with counter > 3
            supportPrice = getPriceEqualCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)).toString()
        } else if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.supportPrices, lastTrade) > 1) {
            //get the max counter
            if (getNumberOfPricesThatHaveCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)) == 1) {
                //choose the price with that maxCounter
                supportPrice = getPriceEqualCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)).toString()
            } else if (getNumberOfPricesThatHaveCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)) > 1) {
                //get lowest price with counter value
                supportPrice = (getLowestPriceWithCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices))).toString()
            }
        }
        modifySupportPrices(ConstantUtils.supportPrices, currentPrice)
    }

    private fun setResistancePrice(currentPrice: Double, lastTrade: Trade) {
        if (ConstantUtils.resistancePrices.isNotEmpty()) {
            val prices = StringBuilder()
            ConstantUtils.resistancePrices.map { prices.append("[${it.price}, ${it.counter}]") }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - setResistancePrice " + "ResistancePrices: $prices " + "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }

        //Get the number of prices counter more 2
        if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.resistancePrices, lastTrade) == 1) {
            //Only 1 price with counter > 2
            resistancePrice = getPriceEqualCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)).toString()
        } else if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.resistancePrices, lastTrade) > 1) {
            //get the max counter
            if (getNumberOfPricesThatHaveCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)) == 1) {
                //choose the price with that maxCounter
                resistancePrice = getPriceEqualCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)).toString()
            } else if (getNumberOfPricesThatHaveCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)) > 1) {
                //get highest price with counter value
                resistancePrice = (getHighestPriceWithCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices))).toString()
            }
        }
        modifyResistancePrices(ConstantUtils.resistancePrices, currentPrice)
    }

    private fun getDifferenceBetweenPrices(priceA: Double, priceB: Double): Double {
        var toReturn: Double? = null
        toReturn = priceA - priceB
        return toReturn
    }

    private fun pullOutOfAsk(currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance, lastAskOrder: Order) {
        if (lastAskOrder.limitPrice.isNotBlank()) {
            val percentage = percentage(lastAskOrder.limitPrice.toDouble(), ConstantUtils.trailingStop)
            if (percentage != null) {
                val precision = precision(percentage)
                if (precision != null) {
                    val result = precision(lastAskOrder.limitPrice.toDouble() - precision)
                    if (result != null && currentPrice <= result) {
                        attachStopOrderObserver(lastAskOrder.id, currentPrice, lastTrade, xrpBalance, zarBalance)
                        pullOutOfAskPrice = result
                        notify("pullOutOfAsk - (LastAskOrder: " + lastAskOrder.limitPrice + ")", "$currentPrice <= $result")
                    }
                }
            }
        } else {
            ask(false, currentPrice, lastTrade, xrpBalance, zarBalance)
        }
    }

    private fun pullOutOfBid(currentPrice: Double, lastTrade: Trade, xrpBalance: Balance, zarBalance: Balance, lastBidOrder: Order) {
        if (lastBidOrder.limitPrice.isNotBlank()) {
            val percentage = percentage(lastBidOrder.limitPrice.toDouble(), ConstantUtils.trailingStop)
            if (percentage != null) {
                val precision = precision(percentage)
                if (precision != null) {
                    val result = precision(lastBidOrder.limitPrice.toDouble() + precision)
                    if (result != null && currentPrice >= result) {
                        attachStopOrderObserver(lastBidOrder.id, currentPrice, lastTrade, xrpBalance, zarBalance)
                        notify("pullOutOfBidCancel - (LastBidOrder: " + lastBidOrder.limitPrice + ")", "$currentPrice >= $result")
                    }
                }
            }
        } else if (supportPrice != null) {
            bid(false, currentPrice, lastTrade, zarBalance)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeMessages(0)
    }

    private fun notify(title: String?, message: String?) {
        /*if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "BotCoin"
            val channel = NotificationChannel(CHANNEL_ID,
                    "BotCoin",
                    NotificationManager.IMPORTANCE_DEFAULT)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
            val notification = Notification.Builder(applicationContext)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.botcoin)
                    .setChannelId(CHANNEL_ID)
                    .build()
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            val notification = Notification.Builder(this)
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .addAction(R.drawable.luno_icon, "Action 1", pendingIntent)
                    .setContentIntent(pendingIntent).notification
            notification.flags = Notification.FLAG_AUTO_CANCEL
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            val notification = Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .build()
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }*/
    }
}