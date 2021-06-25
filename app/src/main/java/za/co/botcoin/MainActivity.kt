package za.co.botcoin

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.json.JSONObject
import za.co.botcoin.databinding.ActivityMainBinding
import za.co.botcoin.navigation.fragments.HomeFrag
import za.co.botcoin.navigation.fragments.MenuFrag
import za.co.botcoin.navigation.fragments.TradeFrag
import za.co.botcoin.navigation.fragments.WalletFrag
import za.co.botcoin.policies.DisclaimerPolicyFrag
import za.co.botcoin.policies.PrivacyPolicyFrag
import za.co.botcoin.settings.fragments.AutoTradeFrag
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.FragmentUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPreferencesUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        //set the custom toolbar
        val toolbar = findViewById<View>(R.id.app_bar) as Toolbar
        setSupportActionBar(toolbar)
        wireUI()
        displayPrivacyPolicy()
    }

    private fun displayPrivacyPolicy() {
        try {
            var jsonObject = SharedPreferencesUtils.get(this, SharedPreferencesUtils.PRIVACY_POLICY_ACCEPTANCE)
            if (jsonObject == null) {
                setNavIcons(true, false, false, false)
                this.binding.navContainer.btnHome.isClickable = false
                this.binding.navContainer.btnBot.isClickable = false
                this.binding.navContainer.btnWallet.isClickable = false
                this.binding.navContainer.btnMenu.isClickable = false
                val privacyPolicyFrag = PrivacyPolicyFrag()
                FragmentUtils.startFragment(supportFragmentManager, privacyPolicyFrag, R.id.fragContainer, supportActionBar, "Privacy Policy", true, false, true, null)
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
    }

    private fun wireUI() {
        setBtnHomeOnClickListener()
        setBtnBotOnClickListener()
        setBtnWalletOnClickListener()
        setBtnMenuOnClickListener()
    }

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
                val autoTradeFrag = AutoTradeFrag()
                FragmentUtils.startFragment(supportFragmentManager, autoTradeFrag, R.id.fragContainer, supportActionBar, "Auto Trade", true, false, true, null)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBtnHomeOnClickListener() {
        this.binding.navContainer.btnHome.setOnClickListener {
            setNavIcons(true, false, false, false)
            val homeFrag = HomeFrag()
            FragmentUtils.startFragment(supportFragmentManager, homeFrag, R.id.fragContainer, supportActionBar, "Home", true, false, true, null)
        }
    }

    private fun setBtnBotOnClickListener() {
        this.binding.navContainer.btnBot.setOnClickListener {
            setNavIcons(false, true, false, false)
            val tradeFrag = TradeFrag()
            FragmentUtils.startFragment(supportFragmentManager, tradeFrag, R.id.fragContainer, supportActionBar, "Trade", true, false, true, null)
        }
    }

    private fun setBtnWalletOnClickListener() {
        this.binding.navContainer.btnWallet.setOnClickListener {
            setNavIcons(false, false, true, false)
            val walletFrag = WalletFrag()
            FragmentUtils.startFragment(supportFragmentManager, walletFrag, R.id.fragContainer, supportActionBar, "Wallet", true, false, true, null)
        }
    }

    private fun setBtnMenuOnClickListener() {
        this.binding.navContainer.btnMenu.setOnClickListener {
            setNavIcons(false, false, false, true)
            val menuFrag = MenuFrag()
            FragmentUtils.startFragment(supportFragmentManager, menuFrag, R.id.fragContainer, supportActionBar, "Menu", true, false, true, null)
        }
    }

    private fun setNavIcons(isHome: Boolean, isBot: Boolean, isWallet: Boolean, isMenu: Boolean) {
        if (isHome) {
            this.binding.navContainer.btnHome.setBackgroundResource(R.drawable.home_fill)
        } else {
            this.binding.navContainer.btnHome.setBackgroundResource(R.drawable.home)
        }
        if (isBot) {
            this.binding.navContainer.btnBot.setBackgroundResource(R.drawable.bot_fill)
        } else {
            this.binding.navContainer.btnBot.setBackgroundResource(R.drawable.bot)
        }
        if (isWallet) {
            this.binding.navContainer.btnWallet.setBackgroundResource(R.drawable.wallet_fill)
        } else {
            this.binding.navContainer.btnWallet.setBackgroundResource(R.drawable.wallet)
        }
        if (isMenu) {
            this.binding.navContainer.btnMenu.setBackgroundResource(R.drawable.menu_fill)
        } else {
            this.binding.navContainer.btnMenu.setBackgroundResource(R.drawable.menu)
        }
    }
}