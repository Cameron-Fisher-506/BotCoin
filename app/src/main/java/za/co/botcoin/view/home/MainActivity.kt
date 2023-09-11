package za.co.botcoin.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.ActivityMainBinding
import za.co.botcoin.model.websocket.dto.Create
import za.co.botcoin.model.websocket.dto.Delete
import za.co.botcoin.model.websocket.dto.OrderBookResponse
import za.co.botcoin.model.websocket.dto.Status
import za.co.botcoin.model.websocket.dto.Trade
import za.co.botcoin.view.BaseActivity
import za.co.botcoin.view.menu.MenuActivity
import za.co.botcoin.view.orderBook.OrderBookActivity
import za.co.botcoin.view.wallet.WalletActivity

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val homeViewModel by viewModels<HomeViewModel>(factoryProducer = { this.getViewModelFactory })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        setUpViews()
        attachNavController()

        val client: OkHttpClient = OkHttpClient()

        val request: Request = Request
            .Builder()
            .url("wss://ws.luno.com/api/1/stream/XRPZAR")
            .build()

        val webSocketLister: WebSocketListener = object: WebSocketListener() {
            private val NORMAL_CLOSURE_STATUS = 1000

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
                homeViewModel.cacheOrderBookResponse(orderBookResponse)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(NORMAL_CLOSURE_STATUS, null)
                Log.d("WebSocket", "Closing : $code $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("WebSocket", "Error: ${t.message}")
            }
        }
        val webSocket: WebSocket = client.newWebSocket(request, webSocketLister)
    }

    private fun attachNavController() {
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun setUpViews() {
        binding.bottomNavigationView.selectedItemId = R.id.home
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.orderBook -> startActivity(Intent(this, OrderBookActivity::class.java))
                R.id.wallet -> startActivity(Intent(this, WalletActivity::class.java))
                R.id.menu -> startActivity(Intent(this, MenuActivity::class.java))
            }
            true
        }
    }
}