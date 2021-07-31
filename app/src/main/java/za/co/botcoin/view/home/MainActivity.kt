package za.co.botcoin.view.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import za.co.botcoin.R
import za.co.botcoin.databinding.ActivityMainBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.SharedPrefsUtils
import za.co.botcoin.view.menu.MenuActivity
import za.co.botcoin.view.settings.AutoTradeActivity
import za.co.botcoin.view.trade.TradeActivity
import za.co.botcoin.view.wallet.WalletActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        wireUI()
        attachNavController()
    }

    private fun attachNavController() {
        this.navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, this.navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(this.navController, null)
    }

    private fun wireUI() {
        this.binding.bottomNavigationView.selectedItemId = R.id.home
        this.binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.trade -> startActivity(Intent(this, TradeActivity::class.java))
                R.id.wallet -> startActivity(Intent(this, WalletActivity::class.java))
                R.id.menu -> startActivity(Intent(this, MenuActivity::class.java))
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.autoTrade -> {

                //auto trade
                startActivity(Intent(this, AutoTradeActivity::class.java))
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}