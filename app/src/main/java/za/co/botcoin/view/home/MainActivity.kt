package za.co.botcoin.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.ActivityMainBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPreferencesUtils
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
        //displayPrivacyPolicy()
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

    /*private fun displayPrivacyPolicy() {
        try {
            var jsonObject = SharedPreferencesUtils.get(this, SharedPreferencesUtils.PRIVACY_POLICY_ACCEPTANCE)
            if (jsonObject == null) {

                return
            }
            jsonObject = SharedPreferencesUtils.get(this, SharedPreferencesUtils.DISCLAIMER_ACCEPTANCE)
            if (jsonObject == null) {
                setNavIcons(true, false, false, false)
                this.binding.navContainer.btnHome.isClickable = false
                this.binding.navContainer.btnBot.isClickable = false
                this.binding.navContainer.btnWallet.isClickable = false
                this.binding.navContainer.btnMenu.isClickable = false
                val disclaimerPolicyFrag = DisclaimerPolicyFrag()
                FragmentUtils.startFragment(supportFragmentManager, disclaimerPolicyFrag, R.id.fragContainer, supportActionBar, "Disclaimer Policy", true, false, true, null)
                return
            }

            //Pull-out bid price
            saveDefaultPullOutBidPrice()
            setUserPulloutBidPrice()
            val homeFrag = HomeFrag()
            FragmentUtils.startFragment(supportFragmentManager, homeFrag, R.id.fragContainer, supportActionBar, "Home", true, false, true, null)


            //set to home initially
            setNavIcons(true, false, false, false)
            GeneralUtils.runAutoTrade(applicationContext)
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: MainActivity - displayPrivacyPolicy " +
                    "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }
    }*/

    private fun saveDefaultPullOutBidPrice() {
        try {
            if (SharedPreferencesUtils.get(applicationContext, SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT) == null) {
                val jsonObject = JSONObject()
                jsonObject.put(SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT, ConstantUtils.trailingStop)
                SharedPreferencesUtils.save(applicationContext, SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT, jsonObject)
            }
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: MainActivity - saveDefaultPullOutBidPrice " +
                    "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }
    }

    private fun setUserPulloutBidPrice() {
        try {
            if (SharedPreferencesUtils.get(applicationContext, SharedPreferencesUtils.PULLOUT_BID_PRICE_USER) != null) {
                val jsonObject = SharedPreferencesUtils.get(applicationContext, SharedPreferencesUtils.PULLOUT_BID_PRICE_USER)
                if (jsonObject != null && jsonObject.has(SharedPreferencesUtils.PULLOUT_BID_PRICE_USER)) {
                    ConstantUtils.trailingStop = jsonObject.getInt(SharedPreferencesUtils.PULLOUT_BID_PRICE_USER)
                }
            }
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: MainActivity - setUserPulloutBidPrice " +
                    "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
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