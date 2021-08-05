package za.co.botcoin.utils

object ConstantUtils {
    var KEY_ID = ""
    var SECRET_KEY = ""

    var USER_KEY_ID: String = ""
    var USER_SECRET_KEY: String = ""

    const val BOTCOIN_TAG = "BOTCOIN"
    const val BTC = "XBT"
    const val ETH = "ETH"
    const val LTC = "LTC"
    const val XRP = "XRP"
    const val ZAR = "ZAR"

    const val TICKER_RUN_TIME: Long = 5000L

    const val PAIR_XRPZAR = "XRPZAR"

    var SUPPORT_PRICE: String? = null
    var RESISTANCE_PRICE: String? = null

    var trailingStop = 10
    var trailingStart = 5

    const val BUY_SELL_MARGIN_PRICE = 0.01

    var supportPriceCounter = 4
    var resistancePriceCounter = 4
}