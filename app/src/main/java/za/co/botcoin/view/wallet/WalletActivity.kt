package za.co.botcoin.view.wallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import za.co.botcoin.view.home.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.WalletActivityBinding
import za.co.botcoin.view.BaseActivity
import za.co.botcoin.view.menu.MenuActivity
import za.co.botcoin.view.orderBook.OrderBookActivity
import za.co.botcoin.view.trade.TradeActivity

class WalletActivity : BaseActivity() {
    private lateinit var binding: WalletActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = WalletActivityBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        wireUI()
        attacheNavController()
    }

    private fun attacheNavController() {
        this.navController = Navigation.findNavController(this, R.id.navHostFragment)
        //NavigationUI.setupWithNavController(this, this.navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(this.navController, null)
    }

    private fun wireUI() {
        this.binding.bottomNavigationView.selectedItemId = R.id.wallet
        this.binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.orderBook -> startActivity(Intent(this, OrderBookActivity::class.java))
                R.id.menu -> startActivity(Intent(this, MenuActivity::class.java))
            }
            true
        }
    }
}