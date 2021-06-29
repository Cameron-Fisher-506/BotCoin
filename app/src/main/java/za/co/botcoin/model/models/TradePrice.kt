package za.co.botcoin.model.models

class TradePrice {
    var counter: Int = 0
    var price: Double = 0.0
    var isIncreased: Boolean = false

    constructor(price: Double, isIncreased: Boolean) {
        this.price = price
        this.isIncreased = isIncreased
    }
}