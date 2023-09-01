package za.co.botcoin.model.websocket

import android.util.Log
import com.google.gson.JsonObject
import okhttp3.Response
import okhttp3.WebSocketListener
import okhttp3.WebSocket
import org.json.JSONObject
import za.co.botcoin.model.websocket.dto.Create
import za.co.botcoin.model.websocket.dto.Delete
import za.co.botcoin.model.websocket.dto.OrderBookResponse
import za.co.botcoin.model.websocket.dto.Status
import za.co.botcoin.model.websocket.dto.Trade

class WebSocketListener : WebSocketListener() {
    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        val jsonString = """{
                "api_key_id": "eccmj8bsdu7b6",
                "api_key_secret": "GXjzrcjO7Q1XP4zipUogGh8AfzA9WKDO-logkXpE7Ak"
            }"""
        val jsonObject = JSONObject(jsonString)
        webSocket.send(jsonObject.toString())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val jsonObject = JSONObject(text)
        val orderBookResponse = OrderBookResponse()
        orderBookResponse.sequence = jsonObject.getString("sequence")
        /*val tradeUpdatesLength = jsonObject.getJSONArray("trade_updates").length()
        for (i in 0..tradeUpdatesLength) {
            val tradeJSONObject = jsonObject.getJSONArray("trade_updates").getJSONObject(i)
            val trade: Trade = Trade().apply {
                sequence = tradeJSONObject.getString("sequence")
                base = tradeJSONObject.getString("base")
                counter = tradeJSONObject.getString("counter")
                makerOrderId = tradeJSONObject.getString("maker_order_id")
                takerOrderId = tradeJSONObject.getString("taker_order_id")
            }
            orderBookResponse.tradeUpdates.add(trade)
        }
        val createJSONObject = jsonObject.getJSONObject("create_update")
        orderBookResponse.createUpdate = Create().apply {
            orderId = createJSONObject.getString("order_id")
            type = createJSONObject.getString("type")
            price = createJSONObject.getString("price")
            volume = createJSONObject.getString("volume")
        }
        val deleteJSONObject = jsonObject.getJSONObject("delete_update")
        orderBookResponse.deleteUpdate = Delete().apply {
            orderId = deleteJSONObject.getString("order_id")
        }
        val statusJSONObject = jsonObject.getJSONObject("status_update")
        orderBookResponse.statusUpdate = Status().apply {
            status = statusJSONObject.getString("status")
        }*/
        orderBookResponse.timestamp = jsonObject.getLong("timestamp")
        Log.d("WebSocket", "Received : ${orderBookResponse.sequence}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        Log.d("WebSocket", "Closing : $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("WebSocket", "Error: ${t.message}")
    }
}