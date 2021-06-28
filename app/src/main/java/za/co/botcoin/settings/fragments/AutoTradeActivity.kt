package za.co.botcoin.settings.fragments

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import za.co.botcoin.R
import za.co.botcoin.databinding.AutoTradeActivityBinding

class AutoTradeActivity : AppCompatActivity() {
    private lateinit var binding: AutoTradeActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        this.binding = AutoTradeActivityBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        attachNavController()
    }

    private fun attachNavController() {
        this.navController = Navigation.findNavController(this, R.id.bottomNavigationView)
        NavigationUI.setupActionBarWithNavController(this, this.navController )
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(this.navController, null)
    }
}