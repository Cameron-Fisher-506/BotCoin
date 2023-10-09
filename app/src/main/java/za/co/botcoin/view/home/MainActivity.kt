package za.co.botcoin.view.home

import android.content.Intent
import android.os.Build
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
import za.co.botcoin.services.FiboService
import za.co.botcoin.services.OrderBookService
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
        startOrderBookService()
    }

    private fun startOrderBookService() {
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(Intent(this, OrderBookService::class.java))
        } else {
            startService(Intent(this, OrderBookService::class.java))
        }
    }

    private fun attachNavController() {
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        //NavigationUI.setupWithNavController(binding.toolbar, navController)
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