package za.co.botcoin.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.enum.Status
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.TradePrice
import za.co.botcoin.model.repository.TickersRepository
import za.co.botcoin.utils.*
import za.co.botcoin.utils.GeneralUtils.buildListTrades
import za.co.botcoin.utils.GeneralUtils.buildPostOrder
import za.co.botcoin.utils.GeneralUtils.buildSend
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.getAuth
import za.co.botcoin.utils.GeneralUtils.getCurrentDateTime
import za.co.botcoin.utils.MathUtils.percentage
import za.co.botcoin.utils.MathUtils.precision
import java.util.*

class BotService : Service(), WSCallUtilsCallBack {
    private lateinit var tickersRepository: TickersRepository
    private val BUY_REQ_CODE = 101
    private val SELL_REQ_CODE = 102
    private val TICKERS_REQ_CODE = 103
    private val LISTTRADES_REQ_CODE = 104
    private val BALANCE_REQ_CODE = 105
    private val REQ_CODE_LAST_TRADE_TYPE = 106
    private val REQ_CODE_SEND = 107
    private val REQ_CODE_FUNDING_ADDRESS = 108
    private val REQ_CODE_ORDERS = 109
    private val REQ_CODE_STOP_ORDER = 110

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

                //getLastPurchase
                lastPurchase
                Log.d(ConstantUtils.BOTCOIN_TAG, "AUTO TRADE RUNNING...")
            }
        }, delay)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun init() {
        this.tickersRepository = TickersRepository(application)

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

    private val walletBalance: Unit
        private get() {
            WSCallsUtils.get(this, BALANCE_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_BALANCE, getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
        }

    private fun bid(isRestrict: Boolean) {
        if (isRestrict) {
            if (supportPrice != null && lastTradeType != ConstantUtils.TRADE_TYPE_BID && supportPrice!!.toDouble() < currentPrice!!.toDouble()) {
                Log.d(ConstantUtils.BOTCOIN_TAG, """
     
     
     Method: BotService - bid
     supportPrice: ${supportPrice}
     lastTradeType: ${lastTradeType}
     currentPrice: ${currentPrice}
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                val amountXrpToBuy = Integer.toString(calcAmountXrpToBuy(zarBalance!!.toDouble(), supportPrice!!.toDouble()))
                val postOrder = buildPostOrder(ConstantUtils.PAIR_XRPZAR, "BID", amountXrpToBuy, supportPrice)
                WSCallsUtils.post(this, BUY_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "", getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
            } else {
                Log.d(ConstantUtils.BOTCOIN_TAG, """
     
     
     Method: BotService - bid
     supportPrice: ${supportPrice}
     lastTradeType: ${lastTradeType}
     currentPrice: ${currentPrice}
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
            }
        } else {
            if (supportPrice != null) {
                val percentage = percentage(supportPrice!!.toDouble(), ConstantUtils.trailingStop)
                if (percentage != null) {
                    val precision = precision(percentage)
                    if (precision != null) {
                        val result = precision(supportPrice!!.toDouble() + precision)
                        if (currentPrice != null && currentPrice!!.toDouble() >= result!!) {
                            notify("bid isRestrict: false - (bid reset support: " + supportPrice + ")", currentPrice + ">=" + result)
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
                Log.d(ConstantUtils.BOTCOIN_TAG, """
     
     
     Method: BotService - ask
     resistancePrice: ${resistancePrice}
     lastTradeType: ${lastTradeType}
     lastPurchasePrice: ${lastPurchasePrice}
     currentPrice: ${currentPrice}
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
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
                            notify("ask - (ResistancePrice: " + resistancePrice + ")", currentPrice + "<=" + result)
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
                            notify("ask - (LastPurchasePrice: " + lastPurchasePrice + ")", currentPrice + "<=" + result)
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
            if (resistancePrice != null) {
                postOrder = buildPostOrder(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell, resistancePrice)
            } else if (newSellPrice != null) {
                postOrder = buildPostOrder(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell, newSellPrice)
            }
            if (postOrder != null) {
                WSCallsUtils.post(this, SELL_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "", getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
            } else {
                Log.d(ConstantUtils.BOTCOIN_TAG, """
     
     
     Method: BotService - ask
     postOrder: null
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
            }
        }
        Log.d(ConstantUtils.BOTCOIN_TAG, """
     
     
     Method: BotService - ask
     resistancePrice: ${resistancePrice}
     lastTradeType: ${lastTradeType}
     lastPurchasePrice: ${lastPurchasePrice}
     currentPrice: ${currentPrice}
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
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

    private fun getMaxCounter(tradePrices: List<TradePrice>?): Int {
        var toReturn = 0
        if (tradePrices != null && tradePrices.size > 0) {
            toReturn = tradePrices[0].counter
            for (i in tradePrices.indices) {
                if (toReturn < tradePrices[i].counter) {
                    toReturn = tradePrices[i].counter
                }
            }
        }
        return toReturn
    }

    private fun getNumberOfPricesThatHaveCounter(tradePrices: List<TradePrice>?, counter: Int): Int {
        var toReturn = 0
        if (tradePrices != null && tradePrices.size > 0) {
            for (i in tradePrices.indices) {
                if (tradePrices[i].counter == counter) {
                    toReturn++
                }
            }
        }
        return toReturn
    }

    private fun getPriceEqualCounter(tradePrices: List<TradePrice>?, counter: Int): Double {
        var toReturn = 0.0
        if (tradePrices != null && tradePrices.size > 0) {
            for (i in tradePrices.indices) {
                if (tradePrices[i].counter == counter) {
                    toReturn = tradePrices[i].price
                }
            }
        }
        return toReturn
    }

    private fun modifySupportPrices(supportPrices: List<TradePrice>?, currentPrice: Double) {
        addPriceToList(supportPrices, currentPrice, false)

        //check if the current price increases above the temp support price
        if (supportPrices != null && supportPrices.size > 0) {
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

    private fun modifyResistancePrices(resistancePrices: List<TradePrice>?, currentPrice: Double) {
        addPriceToList(resistancePrices, currentPrice, true)

        //check if the current price increases above the temp support price
        if (resistancePrices != null && resistancePrices.size > 0) {
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

    private fun addPriceToList(tradePrices: List<TradePrice>?, currentPrice: Double, isIncreased: Boolean) {
        if (tradePrices != null && tradePrices.size > 0) {
            for (i in tradePrices.indices) {
                if (currentPrice == tradePrices[i].price) {
                    return
                }
            }

            //price is not in the list
            tradePrices.add(TradePrice(currentPrice, isIncreased))
        } else tradePrices?.add(TradePrice(currentPrice, isIncreased))
    }

    private fun getLowestPriceWithCounter(tradePrices: List<TradePrice>?, maxCounter: Int): Double? {
        var toReturn: Double? = null
        if (tradePrices != null && tradePrices.size > 0) {
            toReturn = tradePrices[0].price
            val prices = StringBuilder()
            for (i in tradePrices.indices) {
                prices.append("""
    [${tradePrices[i].price}, ${tradePrices[i].counter}]
    
    """.trimIndent())
                if (maxCounter == tradePrices[i].counter && toReturn!! > tradePrices[i].price) {
                    toReturn = tradePrices[i].price
                }
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, """
     
     
     Method: BotService - getLowestPriceWithCounter
     Prices: $prices
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
        }
        return toReturn
    }

    private fun getHighestPriceWithCounter(tradePrices: List<TradePrice>?, maxCounter: Int): Double? {
        var toReturn: Double? = null
        if (tradePrices != null && tradePrices.size > 0) {
            toReturn = tradePrices[0].price
            for (i in tradePrices.indices) {
                if (maxCounter == tradePrices[i].counter && toReturn!! < tradePrices[i].price) {
                    toReturn = tradePrices[i].price
                }
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, """
     
     
     Method: BotService - getHighestPriceWithCounter
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
        }
        return toReturn
    }

    private fun setSupportPrice() {
        if (ConstantUtils.supportPrices != null && ConstantUtils.supportPrices.size > 0) {
            val prices = StringBuilder()
            for (i in ConstantUtils.supportPrices.indices) {
                prices.append("""
    [${ConstantUtils.supportPrices[i].price}, ${ConstantUtils.supportPrices[i].counter}]
    
    """.trimIndent())
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, """
     
     
     Method: BotService - setSupportPrice
     SupportPrices: $prices
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
        }


        //Get the number of prices counter more than 2
        if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.supportPrices) == 1) {
            //Only 1 price with counter > 3
            supportPrice = java.lang.Double.toString(getPriceEqualCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)))
        } else if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.supportPrices) > 1) {
            //get the max counter
            if (getNumberOfPricesThatHaveCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)) == 1) {
                //choose the price with that maxCounter
                supportPrice = java.lang.Double.toString(getPriceEqualCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)))
            } else if (getNumberOfPricesThatHaveCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices)) > 1) {
                //get lowest price with counter value
                supportPrice = java.lang.Double.toString(getLowestPriceWithCounter(ConstantUtils.supportPrices, getMaxCounter(ConstantUtils.supportPrices))!!)
            }
        }
        modifySupportPrices(ConstantUtils.supportPrices, currentPrice!!.toDouble())
    }

    private fun setResistancePrice() {
        if (ConstantUtils.resistancePrices != null && ConstantUtils.resistancePrices.size > 0) {
            val prices = StringBuilder()
            for (i in ConstantUtils.resistancePrices.indices) {
                prices.append("""
    [${ConstantUtils.resistancePrices[i].price}, ${ConstantUtils.resistancePrices[i].counter}]
    
    """.trimIndent())
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, """
     
     
     Method: BotService - setResistancePrice
     ResistancePrices: $prices
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
        }

        //Get the number of prices counter more 2
        if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.resistancePrices) == 1) {
            //Only 1 price with counter > 2
            resistancePrice = java.lang.Double.toString(getPriceEqualCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)))
        } else if (getNumberOfPricesCounterMoreThanTwo(ConstantUtils.resistancePrices) > 1) {
            //get the max counter
            if (getNumberOfPricesThatHaveCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)) == 1) {
                //choose the price with that maxCounter
                resistancePrice = java.lang.Double.toString(getPriceEqualCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)))
            } else if (getNumberOfPricesThatHaveCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices)) > 1) {
                //get highest price with counter value
                resistancePrice = java.lang.Double.toString(getHighestPriceWithCounter(ConstantUtils.resistancePrices, getMaxCounter(ConstantUtils.resistancePrices))!!)
            }
        }
        modifyResistancePrices(ConstantUtils.resistancePrices, currentPrice!!.toDouble())
    }

    private val lastPurchase: Unit
        private get() {
            WSCallsUtils.get(this, LISTTRADES_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_LIST_TRADES + buildListTrades(ConstantUtils.PAIR_XRPZAR, true), getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
        }

    private fun send(address: String, tag: String) {
        WSCallsUtils.post(this, REQ_CODE_SEND, StringUtils.GLOBAL_LUNO_URL + buildSend(ConstantUtils.SERVICE_FEE, ConstantUtils.XRP, address, tag), "", getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
    }

    private val botCoinAccountDetails: Unit
        private get() {
            WSCallsUtils.get(this, REQ_CODE_FUNDING_ADDRESS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_FUNDING_ADDRESS + "?asset=" + ConstantUtils.XRP, getAuth(ConstantUtils.KEY_ID, ConstantUtils.SECRET_KEY))
        }

    private fun getDifferenceBetweenPrices(priceA: Double, priceB: Double): Double {
        var toReturn: Double? = null
        toReturn = priceA - priceB
        return toReturn
    }

    private val listOrders: Unit
        private get() {
            WSCallsUtils.get(this, REQ_CODE_ORDERS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_LISTORDERS, getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
        }

    private fun cancelOrder(idOrder: String) {
        val url = StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_STOP_ORDER + "?order_id=" + idOrder
        WSCallsUtils.post(this, REQ_CODE_STOP_ORDER, url, "", getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
    }

    private fun pullOutOfAsk() {
        if (lastAskOrder != null) {
            val percentage = percentage(lastAskOrder!!.limitPrice.toDouble(), ConstantUtils.trailingStop)
            if (percentage != null) {
                val precision = precision(percentage)
                if (precision != null) {
                    val result = precision(lastAskOrder!!.limitPrice.toDouble() - precision)
                    if (currentPrice != null && currentPrice!!.toDouble() <= result!!) {
                        cancelOrder(lastAskOrder!!.id)
                        pullOutOfAskPrice = result
                        notify("pullOutOfAsk - (LastAskOrder: " + lastAskOrder!!.limitPrice + ")", currentPrice + "<=" + result)
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
                        cancelOrder(lastBidOrder!!.id)
                        notify("pullOutOfBidCancel - (LastBidOrder: " + lastBidOrder!!.limitPrice + ")", currentPrice + ">=" + result)
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

    override fun taskCompleted(response: String?, reqCode: Int) {
        if (response != null) {
            if (reqCode == BUY_REQ_CODE) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("order_id")) {
                        notify("Auto Trade", """
     New buy order has been placed.
     OrderId: ${jsonObject.getString("order_id")}
     """.trimIndent())
                    } else if (jsonObject.has("error")) {
                        notify("Auto Trade", jsonObject.getString("error"))
                    }

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
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: ${e.message}
     Method: BotService - taskCompleted
     reqCode: $reqCode
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                }
            }
            if (reqCode == SELL_REQ_CODE) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("order_id")) {
                        notify("Auto Trade", """
     New sell order has been placed.
     OrderId: ${jsonObject.getString("order_id")}
     """.trimIndent())
                    } else if (jsonObject.has("error")) {
                        notify("Auto Trade", jsonObject.getString("error"))
                    }

                    //empty the the trade price list
                    resistancePrice = null
                    ConstantUtils.resistancePrices.clear()
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: ${e.message}
     Method: BotService - taskCompleted
     reqCode: $reqCode
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                }
            }

            if (reqCode == LISTTRADES_REQ_CODE) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("trades") && !jsonObject.isNull("trades")) {
                        val trades = jsonObject.getJSONArray("trades")
                        if (trades != null && trades.length() > 0) {
                            val trade = trades.getJSONObject(0)
                            val type = trade.getString("type")
                            lastTradeType = type
                            if (lastTradeType == ConstantUtils.TRADE_TYPE_BID) {
                                lastPurchasePrice = trade.getString("price")
                                lastPurchaseVolume = trade.getString("volume")
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
                    walletBalance
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: ${e.message}
     Method: BotService - taskCompleted
     reqCode: $reqCode
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                }
            }
            if (reqCode == BALANCE_REQ_CODE) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("balance")) {
                        val jsonArrayBalance = jsonObject.getJSONArray("balance")
                        if (jsonArrayBalance != null && jsonArrayBalance.length() > 0) {
                            for (i in 0 until jsonArrayBalance.length()) {
                                val jsonObjectBalance = jsonArrayBalance.getJSONObject(i)
                                val currency = jsonObjectBalance.getString("asset")
                                val balance = jsonObjectBalance.getString("balance")
                                val reserved = jsonObjectBalance.getString("reserved")
                                if (currency == ConstantUtils.XRP) {
                                    xrpBalance = balance
                                } else if (currency == ConstantUtils.ZAR) {
                                    zarBalance = balance
                                }
                            }

                            //buy
                            bid(true)

                            //sell
                            ask(true)

                            //get all the orders
                            listOrders
                        }
                    } else {
                        Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: No Response
     Method: BotService - onCreate
     URL: ${StringUtils.GLOBAL_LUNO_URL}/api/1/balance
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: ${e.message}
     Method: BotService - onCreate
     URL: ${StringUtils.GLOBAL_LUNO_URL}/api/1/balance
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                }
            }
            if (reqCode == REQ_CODE_FUNDING_ADDRESS) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("error")) {
                        createAlertDialog(this, "Oops!", jsonObject.getString("error"), false)
                    } else {
                        val address_meta = jsonObject.getJSONArray("address_meta")
                        var address: String? = null
                        var tag: String? = null
                        if (address_meta != null && address_meta.length() > 0) {
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
                            send(address, tag)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: ${e.message}
     Method: BotService - taskCompleted
     Request Code: $reqCode
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                }
            }
            if (reqCode == REQ_CODE_SEND) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null) {
                        notify("Service Fee", jsonObject.toString())
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: ${e.message}
     Method: BotService - taskCompleted
     Request Code: $reqCode
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                }
            }
            if (reqCode == REQ_CODE_ORDERS) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("orders")) {
                        val jsonObjectOrders = jsonObject.getJSONArray("orders")
                        if (jsonObjectOrders != null && jsonObjectOrders.length() > 0) {
                            for (i in 0 until jsonObjectOrders.length()) {
                                val jsonObjectOrder = jsonObjectOrders.getJSONObject(i)
                                val type = jsonObjectOrder.getString("type")
                                val state = jsonObjectOrder.getString("state")
                                val id = jsonObjectOrder.getString("order_id")
                                val pair = jsonObjectOrder.getString("pair")
                                val limitPrice = jsonObjectOrder.getString("limit_price")
                                val limitVolume = jsonObjectOrder.getString("limit_volume")
                                val createdTime = jsonObjectOrder.getString("creation_timestamp")
                                val completedTime = jsonObjectOrder.getString("completed_timestamp")
                                if (type == "ASK" && state == "PENDING") {
                                    lastAskOrder = Order(id, type, state, limitPrice, limitVolume, pair, createdTime, completedTime)
                                } else if (type == "BID" && state == "PENDING") {
                                    lastBidOrder = Order(id, type, state, limitPrice, limitVolume, pair, createdTime, completedTime)
                                }
                            }
                        }
                    }

                    //check if pull out is  necessary
                    pullOutOfAsk()
                    pullOutOfBid()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: ${e.message}
     Method: BotService - taskCompleted
     Request Code: $reqCode
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                }
            }
            if (reqCode == REQ_CODE_STOP_ORDER) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("success")) {
                        notify("Order Cancelled", jsonObject.toString())
                        if (pullOutOfAskPrice != null) {
                            //place new sell order at trailing price
                            resistancePrice = java.lang.Double.toString(pullOutOfAskPrice!!)
                            ask(false)
                        }
                        lastAskOrder = null
                        lastBidOrder = null
                        pullOutOfAskPrice = null
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: ${e.message}
     Method: BotService - taskCompleted
     Request Code: $reqCode
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                }
            }
        } else {
            createAlertDialog(this, "No Signal", "Please check your network connection!", false)
        }
    }

    fun notify(title: String?, message: String?) {
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