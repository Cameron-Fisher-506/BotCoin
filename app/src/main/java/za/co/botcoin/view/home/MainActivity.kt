package za.co.botcoin.view.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import za.co.botcoin.R
import za.co.botcoin.databinding.ActivityMainBinding
import za.co.botcoin.model.websocket.WebSocketListener
import za.co.botcoin.view.BaseActivity
import za.co.botcoin.view.menu.MenuActivity
import za.co.botcoin.view.orderBook.OrderBookActivity
import za.co.botcoin.view.settings.AutoTradeActivity
import za.co.botcoin.view.trade.TradeActivity
import za.co.botcoin.view.wallet.WalletActivity

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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

        val webSocketLister: WebSocketListener = WebSocketListener()
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