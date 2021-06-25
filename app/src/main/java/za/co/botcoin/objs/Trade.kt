package za.co.botcoin.objs

class Trade {
    var type: String = ""
    var amount: String = ""
    var price: String = ""

    companion object {
        const val BUY_TYPE = "BUY"
        const val SELL_TYPE = "SELL"
    }
}