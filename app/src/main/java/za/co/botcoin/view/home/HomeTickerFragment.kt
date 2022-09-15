package za.co.botcoin.view.home

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.HomeFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.services.BotService
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.KioskService
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService

class HomeTickerFragment : HomeBaseFragment(R.layout.home_fragment) {
    private lateinit var binding: HomeFragmentBinding
    private val homeTickerViewModel by viewModels<HomeTickerViewModel>(factoryProducer = { homeActivity.getViewModelFactory })
    private val handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = HomeFragmentBinding.bind(view)

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

    private fun fetchAndObserveTickers() {
        homeTickerViewModel.fetchTickers()
        homeTickerViewModel.tickersResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        displayLinearLayout()

                        data.map { ticker ->
                            if (ticker.pair == ConstantUtils.PAIR_XRPZAR) {
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