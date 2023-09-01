package za.co.botcoin.model.websocket.dto

import com.google.gson.annotations.SerializedName

class Create {
    @SerializedName("order_id")
    var orderId: String = ""
    var type: String = ""
    var price: String = ""
    var volume: String = ""
}