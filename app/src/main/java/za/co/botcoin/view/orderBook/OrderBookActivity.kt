package za.co.botcoin.view.orderBook

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import za.co.botcoin.R
import za.co.botcoin.databinding.OrderBookActivityBinding
import za.co.botcoin.view.BaseActivity
import za.co.botcoin.view.home.MainActivity
import za.co.botcoin.view.menu.MenuActivity
import za.co.botcoin.view.wallet.WalletActivity

class OrderBookActivity : BaseActivity() {
    private lateinit var binding: OrderBookActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = OrderBookActivityBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        setUpViews()
        attachNavController()
    }

    private fun attachNavController() {
        this.navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, this.navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(this.navController, null)
    }

    private fun setUpViews() {
        this.binding.bottomNavigationView.selectedItemId = R.id.wallet
        this.binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.wallet -> startActivity(Intent(this, WalletActivity::class.java))
                R.id.menu -> startActivity(Intent(this, MenuActivity::class.java))
            }
            true
        }
    }
}