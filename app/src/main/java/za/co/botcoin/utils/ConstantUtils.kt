package za.co.botcoin.utils

object ConstantUtils {
    var KEY_ID = "cmpcknw2re6mt"
    var SECRET_KEY = "E3rY3u2cL1JdJr8WcJNjivwjToG3nU-KjR4E25MJeH8"

    var USER_KEY_ID: String = "cmpcknw2re6mt"
    var USER_SECRET_KEY: String = "E3rY3u2cL1JdJr8WcJNjivwjToG3nU-KjR4E25MJeH8"

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
    var smartTrendDetectorMargin = 5

    const val BUY_SELL_MARGIN_PRICE = 0.01

    var supportPriceCounter = 4
    var resistancePriceCounter = 4
}