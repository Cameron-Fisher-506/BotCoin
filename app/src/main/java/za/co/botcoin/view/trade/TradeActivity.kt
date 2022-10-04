package za.co.botcoin.view.trade

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import za.co.botcoin.view.home.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.TradeActivityBinding
import za.co.botcoin.view.menu.MenuActivity
import za.co.botcoin.view.wallet.WalletActivity

class TradeActivity : AppCompatActivity() {
    private lateinit var binding: TradeActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = TradeActivityBinding.inflate(layoutInflater)
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
        this.binding.bottomNavigationView.selectedItemId = R.id.trade
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