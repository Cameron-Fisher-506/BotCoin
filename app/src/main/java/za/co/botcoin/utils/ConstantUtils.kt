package za.co.botcoin.utils

import za.co.botcoin.objs.TradePrice
import java.util.*

object ConstantUtils {
    //KEYS
    @JvmField
    var KEY_ID = "eagwrgxnn8vm"
    @JvmField
    var SECRET_KEY = "J5TyKa7OYuO_OpRuMxovvk-WhQASAhlxtpN0VfyssNw"

    //USER API KEYS
    @JvmField
    var USER_KEY_ID: String? = null
    @JvmField
    var USER_SECRET_KEY: String? = null

    //TAG
    const val BOTCOIN_TAG = "BOTCOIN"

    //CHAR_SET
    const val UTF_8 = "UTF-8"

    //CURRENCY
    const val BTC = "XBT"
    const val ETH = "ETH"
    const val LTC = "LTC"
    const val XRP = "XRP"
    const val ZAR = "ZAR"

    //TICKER
    const val TICKER_RUN_TIME = 5000

    //TICKER PAIRS
    const val PAIR_XRPZAR = "XRPZAR"

    //POST ORDER
    const val TRADE_TYPE_BID = "BID"
    const val TRADE_TYPE_ASK = "ASK"

    //SUPPORT/RESISTANCE
    var SUPPORT_PRICE: String? = null
    var RESISTANCE_PRICE: String? = null
    const val TIME_TO_SET_SUPPORT_RESISTANCE: Long = 30

    //REQUEST METHODS
    const val REQUEST_METHOD_POST = "POST"
    const val REQUEST_METHOD_GET = "GET"

    //SERVICE PRICES
    const val SERVICE_FEE = "0.1"
    const val SERVICE_FEE_MIN_BALANCE = 1.1
    @JvmField
    var trailingStop = 1
    const val PULL_OUT_PRICE = 0.01
    const val BUY_SELL_MARGIN_PRICE = 0.01

    //Support and Resistance Prices
    @JvmField
    var supportPrices: List<TradePrice> = ArrayList()
    @JvmField
    var resistancePrices: List<TradePrice> = ArrayList()
    @JvmField
    var supportPriceCounter = 4
    @JvmField
    var resistancePriceCounter = 4
}