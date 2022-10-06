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
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.KioskService
import za.co.botcoin.view.settings.AutoTradeActivity

class HomeTickerFragment : HomeBaseFragment(R.layout.home_fragment) {
    private lateinit var binding: HomeFragmentBinding
    private val homeTickerViewModel by viewModels<HomeTickerViewModel>(factoryProducer = { homeActivity.getViewModelFactory })
    private val handler = Handler()

    companion object {
        private const val PAIR_XRPZAR = "XRPZAR"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = HomeFragmentBinding.bind(view)

        setUpMenu()
        fetchAndObserveTickers()
        navigateToPrivacyPolicyScreen(view)

        val delay: Long = 100000L
        handler.postDelayed(object : Runnable {
            override fun run() {
                fetchAndObserveTickers()
                if(!KioskService.isMyServiceRunning(requireContext(), BotService::class.java.simpleName)) {
                    GeneralUtils.runAutoTrade(requireContext())
                }
                handler.postDelayed(this, delay)
            }
        }, delay)
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
                        displayLinearLayout()

                        data.map { ticker ->
                            if (ticker.pair.equals(PAIR_XRPZAR, true)) {
                                this.binding.xrpZarTextView.setText(R.string.XRPZAR)
                                this.binding.xrpZarTextView.append(ticker.lastTrade)
                            }
                        }
                    } else {
                        displayErrorTextView()
                    }
                }
                Status.ERROR -> {
                    displayErrorTextView()
                }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun displayLinearLayout() {
        this.binding.xrpZarLinearLayoutCompat.visibility = View.VISIBLE
        this.binding.errorTextView.visibility = View.GONE
    }

    private fun displayErrorTextView() {
        this.binding.xrpZarLinearLayoutCompat.visibility = View.GONE
        this.binding.errorTextView.visibility = View.VISIBLE
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