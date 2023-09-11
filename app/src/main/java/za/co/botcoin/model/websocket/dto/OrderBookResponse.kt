package za.co.botcoin.model.websocket.dto

class OrderBookResponse : BaseResponse() {
    var sequence: String = ""
    var tradeUpdates: ArrayList<Trade> = arrayListOf()
    var createUpdate: Create = Create()
    var deleteUpdate: Delete = Delete()
    var statusUpdate: Status = Status()
    var timestamp: Long = 0L
}