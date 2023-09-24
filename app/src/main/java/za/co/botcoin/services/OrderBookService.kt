package za.co.botcoin.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Index.Order
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IOrderBookDao
import za.co.botcoin.model.room.ITickerDao
import za.co.botcoin.model.websocket.dto.Create
import za.co.botcoin.model.websocket.dto.Delete
import za.co.botcoin.model.websocket.dto.OrderBookResponse
import za.co.botcoin.model.websocket.dto.Status
import za.co.botcoin.model.websocket.dto.Trade
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.cacheService.BaseCacheService
import za.co.botcoin.utils.services.cacheService.BaseCacheService.ORDER_BOOK

class OrderBookService : Service() {
    private lateinit var orderBookDao: IOrderBookDao

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "BotCoin"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "BotCoin",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
            val notification = Notification.Builder(applicationContext)
                .setContentTitle("BotCoin")
                .setContentText("BotCoin Order Book!")
                .setSmallIcon(R.mipmap.botcoin)
                .setChannelId(CHANNEL_ID)
                .build()
            startForeground(1, notification)
        } else {
            GeneralUtils.notify(this, "BotCoin", "Order Book Started")
        }

        orderBookDao = BotCoinDatabase.getDatabase(applicationContext).orderBookDao()
        setUpWebSocket(setUpWebSocketListener())

    }

    private fun setUpWebSocketListener(): WebSocketListener = object : WebSocketListener() {
        private val NORMAL_CLOSURE_STATUS = 1000

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            val jsonString = """{
                "api_key_id": "eccmj8bsdu7b6",
                "api_key_secret": "GXjzrcjO7Q1XP4zipUogGh8AfzA9WKDO-logkXpE7Ak"
            }"""
            val jsonObject = JSONObject(jsonString)
            webSocket.send(jsonObject.toString())

            BaseCacheService.setValue(ORDER_BOOK, OrderBookResponse())
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Message : $text")
            val jsonObject = JSONObject(text)
            val orderBookResponse = OrderBookResponse()
            orderBookResponse.sequence = jsonObject.getString("sequence")

            if (jsonObject.has("asks")) {
                val asksLength = jsonObject.getJSONArray("asks").length()
                for (i in 0 until asksLength) {
                    val askJSONObject = jsonObject.getJSONArray("asks").getJSONObject(i)
                    val create = Create().apply {
                        orderId = askJSONObject.getString("id")
                        price = askJSONObject.getString("price")
                        type = "ASK"
                        volume = askJSONObject.getString("volume")
                    }
                    orderBookResponse.asks.add(create)
                }
                saveCreate(orderBookResponse.asks)
            }

            if (jsonObject.has("bids")) {
                val bidsLength = jsonObject.getJSONArray("bids").length()
                for (i in 0 until bidsLength) {
                    val bidJSONObject = jsonObject.getJSONArray("bids").getJSONObject(i)
                    val create = Create().apply {
                        orderId = bidJSONObject.getString("id")
                        price = bidJSONObject.getString("price")
                        type = "BID"
                        volume = bidJSONObject.getString("volume")
                    }
                    orderBookResponse.bids.add(create)
                }
                saveCreate(orderBookResponse.bids)
            }

            if (!jsonObject.isNull("trade_updates")) {
                val tradeUpdatesLength = jsonObject.getJSONArray("trade_updates").length()
                for (i in 0 until tradeUpdatesLength) {
                    val tradeJSONObject = jsonObject.getJSONArray("trade_updates").getJSONObject(i)
                    val trade: Trade = Trade().apply {
                        sequence = tradeJSONObject.getString("sequence")
                        base = tradeJSONObject.getString("base")
                        counter = tradeJSONObject.getString("counter")
                        makerOrderId = tradeJSONObject.getString("maker_order_id")
                        takerOrderId = tradeJSONObject.getString("taker_order_id")
                    }
                    orderBookResponse.tradeUpdates.add(trade)
                    updateCreate(trade)
                }
            }

            if (!jsonObject.isNull("create_update")) {
                val createJSONObject = jsonObject.getJSONObject("create_update")
                orderBookResponse.createUpdate = Create().apply {
                    orderId = createJSONObject.getString("order_id")
                    type = createJSONObject.getString("type")
                    price = createJSONObject.getString("price")
                    volume = createJSONObject.getString("volume")
                }
                saveCreate(orderBookResponse.createUpdate)
            }

            if (!jsonObject.isNull("delete_update")) {
                val deleteJSONObject = jsonObject.getJSONObject("delete_update")
                orderBookResponse.deleteUpdate = Delete().apply {
                    orderId = deleteJSONObject.getString("order_id")
                }
            }

            if (!jsonObject.isNull("status_update")) {
                val statusJSONObject = jsonObject.getJSONObject("status_update")
                orderBookResponse.statusUpdate = Status().apply {
                    status = statusJSONObject.getString("status")
                }
            }

            orderBookResponse.timestamp = jsonObject.getLong("timestamp")
            BaseCacheService.setValue(ORDER_BOOK, orderBookResponse)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            Log.d("WebSocket", "Closing : $code $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("WebSocket", "Error: ${t.message}")
        }
    }

    fun saveCreate(creates: List<Create>) = CoroutineScope(Dispatchers.IO).launch {
        orderBookDao.insert(creates)
    }

    fun saveCreate(create: Create) = CoroutineScope(Dispatchers.IO).launch {
        orderBookDao.insert(create)
    }

    fun updateCreate(trade: Trade) = CoroutineScope(Dispatchers.IO).launch {
        val create = orderBookDao.getById(trade.makerOrderId)
        val newVolume = create.volume.toDouble() - trade.base.toDouble()
        create.volume = newVolume.toString()
        orderBookDao.update(create)
    }

    private fun setUpWebSocket(webSocketListener: WebSocketListener) {
        val client: OkHttpClient = OkHttpClient()

        val request: Request = Request
            .Builder()
            .url("wss://ws.luno.com/api/1/stream/XRPZAR")
            .build()

        val webSocket: WebSocket = client.newWebSocket(request, webSocketListener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null
}