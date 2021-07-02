package za.co.botcoin.model.models

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["orderId"], unique = true)])
class Trade : BaseModel {
    var type: String = ""
    var volume: String = ""
    var price: String = ""
    var timestamp: String = ""
    var sequence: Int = 0
    var pair: String = ""
    var counter: String = ""
    var base: String = ""

    @SerializedName("client_order_id")
    var clientOrderId: String = ""

    @SerializedName("fee_base")
    var feeBase: String = ""

    @SerializedName("fee_counter")
    var feeCounter: String = ""

    @SerializedName("is_buy")
    var isBuy: Boolean = false

    @SerializedName("order_id")
    var orderId: String = ""

    var accountId: Int = 1

    companion object {
        const val BUY_TYPE = "BUY"
        const val SELL_TYPE = "SELL"
    }

    constructor(type: String, amount: String, price: String) {
        this.type = type
        this.volume = amount
        this.price = price
    }
}