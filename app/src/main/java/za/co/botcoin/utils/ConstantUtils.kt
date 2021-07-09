package za.co.botcoin.utils

import za.co.botcoin.model.models.TradePrice
import java.util.*
import kotlin.collections.ArrayList

object ConstantUtils {
    //KEYS
    @JvmField
    var KEY_ID = ""
    @JvmField
    var SECRET_KEY = ""

    //USER API KEYS
    @JvmField
    var USER_KEY_ID: String = ""
    @JvmField
    var USER_SECRET_KEY: String = ""

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
    const val TICKER_RUN_TIME: Long = 5000L

    //TICKER PAIRS
    const val PAIR_XRPZAR = "XRPZAR"

    //SUPPORT/RESISTANCE
    var SUPPORT_PRICE: String? = null
    var RESISTANCE_PRICE: String? = null
    const val TIME_TO_SET_SUPPORT_RESISTANCE: Long = 30

    //SERVICE PRICES
    const val SERVICE_FEE = "0.1"
    const val SERVICE_FEE_MIN_BALANCE = 1.1
    @JvmField
    var trailingStop = 1
    const val PULL_OUT_PRICE = 0.01
    const val BUY_SELL_MARGIN_PRICE = 0.01


    @JvmField
    var supportPriceCounter = 4
    @JvmField
    var resistancePriceCounter = 4
}