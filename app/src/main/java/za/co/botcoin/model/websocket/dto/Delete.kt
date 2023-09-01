package za.co.botcoin.model.websocket.dto

import com.google.gson.annotations.SerializedName

class Delete {
    @SerializedName("order_id")
    var orderId: String = ""
}