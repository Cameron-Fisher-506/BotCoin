package za.co.botcoin.model.websocket.dto

import com.google.gson.annotations.SerializedName

class OrderBookResponse : BaseResponse() {
    var sequence: String = ""

    @SerializedName("trade_updates")
    var tradeUpdates: ArrayList<Trade> = arrayListOf()

    @SerializedName("create_update")
    var createUpdate: Create = Create()

    @SerializedName("delete_update")
    var deleteUpdate: Delete = Delete()

    @SerializedName("status_update")
    var statusUpdate: Status = Status()
    var timestamp: Long = 0L
}