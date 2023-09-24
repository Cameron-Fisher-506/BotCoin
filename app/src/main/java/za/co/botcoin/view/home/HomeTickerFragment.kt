package za.co.botcoin.view.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.HomeFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.services.BotService
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.KioskService
import za.co.botcoin.view.delayOnLifecycle
import za.co.botcoin.view.home.HomeTickerViewModel.Companion.DELAY
import za.co.botcoin.view.home.HomeTickerViewModel.Companion.PAIR_XRPZAR
import za.co.botcoin.view.settings.AutoTradeActivity

class HomeTickerFragment : HomeBaseFragment(R.layout.home_fragment) {
    private lateinit var binding: HomeFragmentBinding
    private val homeTickerViewModel by viewModels<HomeTickerViewModel>(factoryProducer = { homeActivity.getViewModelFactory })
    private val handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)
        setUpViews()
        setUpMenu()
        //fetchAndObserveTickers()
        navigateToPrivacyPolicyScreen(view)

        val delay: Long = 100000L
        handler.postDelayed(object : Runnable {
            override fun run() {
                //fetchAndObserveTickers()
                if(!KioskService.isMyServiceRunning(homeActivity, BotService::class.java.simpleName)) {
                    GeneralUtils.runAutoTrade(homeActivity)
                }
                handler.postDelayed(this, delay)
            }
        }, delay)

        /*binding.xrpZarLinearLayoutCompat.delayOnLifecycle(DELAY) {
            fetchAndObserveTickers()
            if(!KioskService.isMyServiceRunning(homeActivity, BotService::class.java.simpleName)) {
                GeneralUtils.runAutoTrade(homeActivity)
            }
        }*/
    }

    private fun setUpViews() {
        binding.xrpZarOptionActionView.hideOptionActionDividerView()
        binding.xrpZarOptionActionView.hideOptionActionImageView()
    }

    private fun setUpMenu() {
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }


            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
            }


            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.autoTrade -> {

                        //auto trade
                        startActivity(Intent(homeActivity, AutoTradeActivity::class.java))
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun fetchAndObserveTickers() {
        homeTickerViewModel.fetchTickers()
        homeTickerViewModel.tickersResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        displayCoins()

                        data.forEach { ticker ->
                            if (ticker.pair.equals(PAIR_XRPZAR, true)) {
                                this.binding.xrpZarOptionActionView.setText(getString(R.string.XRPZAR, ticker.lastTrade))
                            }
                        }
                    } else {
                        displayErrorMessage()
                    }
                }
                Status.ERROR -> {
                    displayErrorMessage()
                }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun displayCoins() {
        binding.xrpZarOptionActionView.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE
    }

    private fun displayErrorMessage() {
        binding.xrpZarOptionActionView.visibility = View.GONE
        binding.errorTextView.visibility = View.VISIBLE
    }

    private fun navigateToPrivacyPolicyScreen(view: View) {
        val privacyPolicyAcceptance = homeTickerViewModel.getPrivacyPolicyAcceptance()
        if (privacyPolicyAcceptance == null) {
            val action = HomeTickerFragmentDirections.actionHomeTickerFragmentToPrivacyPolicyFragment()
            Navigation.findNavController(view).navigate(action)
        } else {
            homeTickerViewModel.setUserTrailingStartPrice()
            homeTickerViewModel.setUserTrailingStopPrice()
            homeTickerViewModel.setSupportPriceCounter()
            homeTickerViewModel.setResistancePriceCounter()
            homeTickerViewModel.setSmartTrendDetectorMargin()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeMessages(0)
    }
}