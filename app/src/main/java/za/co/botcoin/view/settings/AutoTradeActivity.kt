package za.co.botcoin.view.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import za.co.botcoin.view.home.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.AutoTradeActivityBinding
import za.co.botcoin.view.trade.TradeActivity

class AutoTradeActivity : AppCompatActivity() {
    private lateinit var binding: AutoTradeActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = AutoTradeActivityBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        wireUI()
        attachNavController()
    }

    private fun attachNavController() {
        this.navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, this.navController )
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(this.navController, null)
    }

    private fun wireUI() {
        this.binding.bottomNavigationView.selectedItemId = R.id.menu
        this.binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.trade -> startActivity(Intent(this, TradeActivity::class.java))
            }
            true
        }
    }
}