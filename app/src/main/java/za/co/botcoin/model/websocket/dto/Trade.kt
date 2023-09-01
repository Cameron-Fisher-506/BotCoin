package za.co.botcoin.model.websocket.dto

import com.google.gson.annotations.SerializedName

class Trade {
    var sequence: String = ""
    var base: String = ""
    var counter: String = ""

    @SerializedName("maker_order_id")
    var makerOrderId: String = ""

    @SerializedName("taker_order_id")
    var takerOrderId: String = ""
}