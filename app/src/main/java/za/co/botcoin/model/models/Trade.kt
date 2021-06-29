package za.co.botcoin.model.models

class Trade : BaseModel {
    var type: String = ""
    var amount: String = ""
    var price: String = ""

    companion object {
        const val BUY_TYPE = "BUY"
        const val SELL_TYPE = "SELL"
    }

    constructor(type: String, amount: String, price: String) {
        this.type = type
        this.amount = amount
        this.price = price
    }
}