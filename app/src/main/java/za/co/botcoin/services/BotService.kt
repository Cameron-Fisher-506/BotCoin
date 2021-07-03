package za.co.botcoin.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import org.json.JSONException
import za.co.botcoin.R
import za.co.botcoin.enum.Status
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.TradePrice
import za.co.botcoin.model.repository.AccountRepository
import za.co.botcoin.model.repository.TickersRepository
import za.co.botcoin.model.repository.WithdrawalRepository
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.MathUtils.percentage
import za.co.botcoin.utils.MathUtils.precision
import za.co.botcoin.utils.SharedPreferencesUtils
import java.util.*

class BotService : Service() {
    private lateinit var tickersRepository: TickersRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var withdrawalRepository: WithdrawalRepository

    private var currentPrice: String? = null

    //LAST PURCHASE
    private var lastPurchasePrice: String? = null
    private var lastPurchaseVolume: String? = null
    private var lastTradeType: String? = null

    //Support/Resistance
    private var supportPrice: String? = null
    private var resistancePrice: String? = null

    //wallet
    private var xrpBalance: String? = null
    private var zarBalance: String? = null

    //Orders
    private var lastAskOrder: Order? = null
    private var lastBidOrder: Order? = null

    //flags
    private var pullOutOfAskPrice: Double? = null
    override fun onCreate() {
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

        val handler = Handler()
        val delay: Long = ConstantUtils.TICKER_RUN_TIME
        handler.postDelayed(object : Runnable {
            override fun run() {
                //get current price
                attachTickersObserver()

                //getLastPurchase
                attachTradesObserver()
                Log.d(ConstantUtils.BOTCOIN_TAG, "AUTO TRADE RUNNING...")
            }
        }, delay)
    }

    private fun attachTickersObserver() {
        tickersRepository.fetchTickers(true).observeForever {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { ticker ->
                            if (ticker.pair == ConstantUtils.PAIR_XRPZAR) {
                                currentPrice = ticker.lastTrade
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
        }
    }

    private fun attachTradesObserver() {
        this.accountRepository.fetchTrades(true, ConstantUtils.PAIR_XRPZAR, true).observeForever {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { trade ->
                            lastTradeType = trade.type
                            if (lastTradeType == ConstantUtils.TRADE_TYPE_BID) {
                                lastPurchasePrice = trade.price
                                lastPurchaseVolume = trade.volume
                                if (currentPrice != null && lastPurchasePrice != null && currentPrice!!.toDouble() > lastPurchasePrice!!.toDouble()) {
                                    setResistancePrice()
                                }
                            } else {
                                setSupportPrice()
                            }
                        }
                    } else {
                        lastTradeType = ConstantUtils.TRADE_TYPE_ASK
                        lastPurchasePrice = "0"
                        //set support/resistance price
                        setSupportPrice()
                        //setResistancePrice();
                    }

                    //Get ZAR and XRP balance
                    attachBalancesObserver()
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    private fun attachBalancesObserver() {
        this.accountRepository.fetchBalances(true).observeForever {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { balance ->
                            val reserved = balance.reserved
                            if (balance.asset == ConstantUtils.XRP) {
                                xrpBalance = balance.balance
                            } else if (balance.asset == ConstantUtils.ZAR) {
                                zarBalance = balance.balance
                            }
                        }

                        //buy
                        bid(true)

                        //sell
                        ask(true)

                        //get all the orders
                        attachOrdersObserver()
                    } else {

                    }
                }
                Status.ERROR -> { }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun attachOrdersObserver() {
        this.accountRepository.fetchOrders(true).observeForever {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { order ->
                            if (order.type == "ASK" && order.state == "PENDING") {
                                lastAskOrder = Order(order.id, order.type, order.state, order.limitPrice, order.limitVolume, order.pair, order.createdTime, order.completedTime)
                            } else if (order.type == "BID" && order.state == "PENDING") {
                                lastBidOrder = Order(order.id, order.type, order.state, order.limitPrice, order.limitVolume, order.pair, order.createdTime, order.completedTime)
                            }
                        }
                    }

                    //check if pull out is  necessary
                    pullOutOfAsk()
                    pullOutOfBid()
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun attachStopOrderObserver(orderId: String) {
        this.withdrawalRepository.stopOrder(true, orderId).observeForever {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        if (data.first().success) {
                            notify("Order Cancellation", "Order cancelled successfully.")

                            if (pullOutOfAskPrice != null) {
                                //place new sell order at trailing price
                                resistancePrice = java.lang.Double.toString(pullOutOfAskPrice!!)
                                ask(false)
                            }
                            lastAskOrder = null
                            lastBidOrder = null
                            pullOutOfAskPrice = null
                        } else {
                            notify("Order Cancellation", "Order cancellation failed.")
                        }
                    } else {
                        notify("Order Cancellation", "Order cancellation failed.")
                    }

                }
                Status.ERROR -> { notify("Order Cancellation", "Order cancellation failed.") }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun attachReceiveObserver() {
        this.withdrawalRepository.receive(true, ConstantUtils.XRP).observeForever {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if(!data.isNullOrEmpty()) {
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
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    private fun attachPostOrderObserver(pair: String, type: String, volume: String, price: String) {
        this.accountRepository.postOrder(true, pair, type, volume, price).observeForever {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {

                    } else {

                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    private fun attachSendObserver(amount: String, currency: String, address: String, destinationTag: String) {
        this.withdrawalRepository.send(true, amount, currency, address, destinationTag).observeForever {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if(!data.isNullOrEmpty()) {
                        data.map { send -> if (send.success) notify("Sent $amount $currency to $address.", send.withdrawalId) else notify("Send failed.", "")}
                    } else {
                        notify("Send failed.", "")
                    }
                }
                Status.ERROR -> { notify("Send failed.", "") }
                Status.LOADING -> {}
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun init() {
        this.tickersRepository = TickersRepository(application)
        this.accountRepository = AccountRepository(application)
        this.withdrawalRepository = WithdrawalRepository(application)

        supportPrice = null
        resistancePrice = null
        lastPurchasePrice = "0"
        lastPurchaseVolume = "0"
        lastTradeType = ""
        xrpBalance = "0"
        zarBalance = "0"
        lastAskOrder = null
        lastBidOrder = null
    }

    private fun bid(isRestrict: Boolean) {
        if (isRestrict) {
            if (supportPrice != null && lastTradeType != ConstantUtils.TRADE_TYPE_BID && supportPrice!!.toDouble() < currentPrice!!.toDouble()) {
                Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - bid " +
                        "supportPrice: $supportPrice " +
                        "lastTradeType: $lastTradeType " +
                        "currentPrice: $currentPrice " +
                        "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                val amountXrpToBuy = calcAmountXrpToBuy(zarBalance!!.toDouble(), supportPrice!!.toDouble()).toString()
                supportPrice?.let {
                    attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR,"BID", amountXrpToBuy, it)
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
                }
            } else {
                Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - bid " +
                        "supportPrice: $supportPrice " +
                        "lastTradeType: $lastTradeType " +
                        "currentPrice: $currentPrice " +
                        "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
            }
        } else {
            if (supportPrice != null) {
                val percentage = percentage(supportPrice!!.toDouble(), ConstantUtils.trailingStop)
                if (percentage != null) {
                    val precision = precision(percentage)
                    if (precision != null) {
                        val result = precision(supportPrice!!.toDouble() + precision)
                        if (currentPrice != null && currentPrice!!.toDouble() >= result!!) {
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

    private fun ask(isRestrict: Boolean) {
        var placeSellOrder = false
        var newSellPrice: String? = null
        if (isRestrict) {
            if (resistancePrice != null && lastTradeType == ConstantUtils.TRADE_TYPE_BID && resistancePrice!!.toDouble() > lastPurchasePrice!!.toDouble() && resistancePrice!!.toDouble() > currentPrice!!.toDouble()) //
            {
                placeSellOrder = true
                Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - ask " +
                        "resistancePrice: $resistancePrice " +
                        "lastTradeType: $lastTradeType " +
                        "lastPurchasePrice: $lastPurchasePrice " +
                        "currentPrice: $currentPrice " +
                        "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
            }
        } else {
            if (resistancePrice != null) {
                val percentage = percentage(resistancePrice!!.toDouble(), ConstantUtils.trailingStop)
                if (percentage != null) {
                    val precision = precision(percentage)
                    if (precision != null) {
                        val result = precision(resistancePrice!!.toDouble() - precision)
                        if (currentPrice != null && currentPrice!!.toDouble() <= result!!) {
                            resistancePrice = java.lang.Double.toString(result)
                            placeSellOrder = true
                            notify("ask - (ResistancePrice: $resistancePrice)", "$currentPrice <= $result")
                            ConstantUtils.supportPriceCounter = 9
                        }
                    }
                }
            } else if (lastPurchasePrice != null && lastPurchasePrice != "0") {
                val percentage = percentage(lastPurchasePrice!!.toDouble(), ConstantUtils.trailingStop)
                if (percentage != null) {
                    val precision = precision(percentage)
                    if (precision != null) {
                        val result = precision(lastPurchasePrice!!.toDouble() - precision)
                        if (currentPrice != null && currentPrice!!.toDouble() <= result!!) {
                            newSellPrice = java.lang.Double.toString(result)
                            placeSellOrder = true
                            notify("ask - (LastPurchasePrice: $lastPurchasePrice)", "$currentPrice <= $result")
                            ConstantUtils.supportPriceCounter = 9
                        }
                    }
                }
            }
        }
        if (placeSellOrder) {
            val newXrpBalance = xrpBalance!!.toDouble()

            /*if (newXrpBalance >= ConstantUtils.SERVICE_FEE_MIN_BALANCE)
            {
                newXrpBalance -= 0.1;
                getBotCoinAccountDetails();
            }*/
            val amountXrpToSell = Integer.toString(calcAmountXrpToSell(newXrpBalance))
            var postOrder: String? = null

            val resistancePriceTemp = resistancePrice
            val newSellPriceTemp = newSellPrice
            when {
                resistancePriceTemp != null -> {
                    attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR,"ASK", amountXrpToSell, resistancePriceTemp)
                    notify("Auto Trade", "New sell order has been placed.")

                    //empty the the trade price list
                    resistancePrice = null
                    ConstantUtils.resistancePrices.clear()
                }

                newSellPriceTemp != null -> {
                    attachPostOrderObserver(ConstantUtils.PAIR_XRPZAR,"ASK", amountXrpToSell, newSellPriceTemp)
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
                "lastTradeType: $lastTradeType " +
                "lastPurchasePrice: $lastPurchasePrice " +
                "currentPrice: $currentPrice " +
                "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
    }

    private fun getNumberOfPricesCounterMoreThanTwo(tradePrices: List<TradePrice>): Int {
        var toReturn = 0
        for (i in tradePrices.indices) {
            if (lastTradeType != null && lastTradeType == "BID") {
                if (tradePrices[i].counter > ConstantUtils.resistancePriceCounter) {
                    toReturn++
                }
            } else if (lastTradeType != null && lastTradeType == "ASK") {
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

    private fun modifySupportPrices(supportPrices: ArrayList<TradePrice>?, currentPrice: Double) {
        addPriceToList(supportPrices, currentPrice, false)

        //check if the current price increases above the temp support price
        if (supportPrices != null && supportPrices.isNotEmpty()) {
            for (i in supportPrices.indices) {
                if (currentPrice > supportPrices[i].price) {
                    supportPrices[i].isIncreased = true
                }
                if (currentPrice == supportPrices[i].price) {
                    if (supportPrices[i].isIncreased) {
                        val currentCounter = supportPrices[i].counter + 1
                        supportPrices[i].counter = currentCounter
                        supportPrices[i].isIncreased = false
                    }
                }
            }
        }
    }

    private fun modifyResistancePrices(resistancePrices: ArrayList<TradePrice>?, currentPrice: Double) {
        addPriceToList(resistancePrices, currentPrice, true)

        //check if the current price increases above the temp support price
        if (resistancePrices != null && resistancePrices.isNotEmpty()) {
            for (i in resistancePrices.indices) {
                if (currentPrice < resistancePrices[i].price) {
                    resistancePrices[i].isIncreased = false
                }
                if (currentPrice == resistancePrices[i].price) {
                    if (!resistancePrices[i].isIncreased) {
                        val currentCounter = resistancePrices[i].counter + 1
                        resistancePrices[i].counter = currentCounter
                        resistancePrices[i].isIncreased = true
                    }
                }
            }
        }
    }

    private fun addPriceToList(tradePrices: ArrayList<TradePrice>?, currentPrice: Double, isIncreased: Boolean) {
        if (tradePrices != null && tradePrices.isNotEmpty()) {
            for (i in tradePrices.indices) {
                if (currentPrice == tradePrices[i].price) {
                    return
                }
            }

            //price is not in the list
            tradePrices.add(TradePrice(currentPrice, isIncreased))
        } else {
            tradePrices?.add(TradePrice(currentPrice, isIncreased))
        }
    }

    private fun getLowestPriceWithCounter(tradePrices: List<TradePrice>?, maxCounter: Int): Double? {
        var toReturn: Double? = null
        if (tradePrices != null && tradePrices.isNotEmpty()) {
            toReturn = tradePrices[0].price
            val prices = StringBuilder()
            for (i in tradePrices.indices) {
                prices.append("[${tradePrices[i].price}, ${tradePrices[i].counter}]")
                if (maxCounter == tradePrices[i].counter && toReturn!! > tradePrices[i].price) {
                    toReturn = tradePrices[i].price
                }
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - getLowestPriceWithCounter " +
                    "Prices: $prices" +
                    "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }
        return toReturn
    }

    private fun getHighestPriceWithCounter(tradePrices: List<TradePrice>?, maxCounter: Int): Double? {
        var toReturn: Double? = null
        if (tradePrices != null && tradePrices.isNotEmpty()) {
            toReturn = tradePrices[0].price
            for (i in tradePrices.indices) {
                if (maxCounter == tradePrices[i].counter && toReturn!! < tradePrices[i].price) {
                    toReturn = tradePrices[i].price
                }
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - getHighestPriceWithCounter " +
                    "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }
        return toReturn
    }

    private fun setSupportPrice() {
        if (ConstantUtils.supportPrices.isNotEmpty()) {
            val prices = StringBuilder()
            for (i in ConstantUtils.supportPrices.indices) {
                prices.append("[${ConstantUtils.supportPrices[i].price}, ${ConstantUtils.supportPrices[i].counter}]")
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - setSupportPrice " +
                    "SupportPrices: $prices " +
                    "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }

        //Get the number of prices counter more than 2
        if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.supportPrices) == 1) {
            //Only 1 price with counter > 3
            supportPrice = getPriceEqualCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)).toString()
        } else if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.supportPrices) > 1) {
            //get the max counter
            if (getNumberOfPricesThatHaveCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)) == 1) {
                //choose the price with that maxCounter
                supportPrice = getPriceEqualCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)).toString()
            } else if (getNumberOfPricesThatHaveCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)) > 1) {
                //get lowest price with counter value
                supportPrice = (getLowestPriceWithCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices))!!).toString()
            }
        }
        modifySupportPrices(ConstantUtils.supportPrices, currentPrice!!.toDouble())
    }

    private fun setResistancePrice() {
        if (ConstantUtils.resistancePrices.isNotEmpty()) {
            val prices = StringBuilder()
            for (i in ConstantUtils.resistancePrices.indices) {
                prices.append("[${ConstantUtils.resistancePrices[i].price}, ${ConstantUtils.resistancePrices[i].counter}]")
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, "Method: BotService - setResistancePrice " +
                    "ResistancePrices: $prices " +
                    "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }

        //Get the number of prices counter more 2
        if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.resistancePrices) == 1) {
            //Only 1 price with counter > 2
            resistancePrice = getPriceEqualCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)).toString()
        } else if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.resistancePrices) > 1) {
            //get the max counter
            if (getNumberOfPricesThatHaveCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)) == 1) {
                //choose the price with that maxCounter
                resistancePrice = getPriceEqualCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)).toString()
            } else if (getNumberOfPricesThatHaveCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)) > 1) {
                //get highest price with counter value
                resistancePrice = (getHighestPriceWithCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices))!!).toString()
            }
        }
        modifyResistancePrices(ConstantUtils.resistancePrices, currentPrice!!.toDouble())
    }

    private fun getDifferenceBetweenPrices(priceA: Double, priceB: Double): Double {
        var toReturn: Double? = null
        toReturn = priceA - priceB
        return toReturn
    }

    private fun pullOutOfAsk() {
        if (lastAskOrder != null) {
            val percentage = percentage(lastAskOrder!!.limitPrice.toDouble(), ConstantUtils.trailingStop)
            if (percentage != null) {
                val precision = precision(percentage)
                if (precision != null) {
                    val result = precision(lastAskOrder!!.limitPrice.toDouble() - precision)
                    if (currentPrice != null && currentPrice!!.toDouble() <= result!!) {
                        attachStopOrderObserver(lastAskOrder!!.id)
                        pullOutOfAskPrice = result
                        notify("pullOutOfAsk - (LastAskOrder: " + lastAskOrder!!.limitPrice + ")", "$currentPrice <= $result")
                    }
                }
            }
        } else {
            ask(false)
        }
    }

    private fun pullOutOfBid() {
        if (lastBidOrder != null) {
            val percentage = percentage(lastBidOrder!!.limitPrice.toDouble(), ConstantUtils.trailingStop)
            if (percentage != null) {
                val precision = precision(percentage)
                if (precision != null) {
                    val result = precision(lastBidOrder!!.limitPrice.toDouble() + precision)
                    if (currentPrice != null && currentPrice!!.toDouble() >= result!!) {
                        attachStopOrderObserver(lastBidOrder!!.id)
                        notify("pullOutOfBidCancel - (LastBidOrder: " + lastBidOrder!!.limitPrice + ")", "$currentPrice >= $result")
                    }
                }
            }
        } else if (supportPrice != null) {
            bid(false)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun notify(title: String?, message: String?) {
        if (Build.VERSION.SDK_INT >= 26) {
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
        }
    }
}