package za.co.botcoin.view.home

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.HomeFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.services.BotService
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.KioskUtils
import za.co.botcoin.utils.SharedPrefsUtils

class HomeFragment : Fragment(R.layout.home_fragment) {
    private lateinit var binding: HomeFragmentBinding
    private lateinit var tickersViewModel: TickersViewModel

    private val handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = HomeFragmentBinding.bind(view)

        this.tickersViewModel = ViewModelProviders.of(this).get(TickersViewModel::class.java)

        attachTickerObserver()
        displayPrivacyPolicy(view)

        val delay: Long = 100000L
        handler.postDelayed(object : Runnable {
            override fun run() {
                attachTickerObserver()
                if(!KioskUtils.isMyServiceRunning(requireContext(), BotService::class.java.simpleName)) {
                    GeneralUtils.runAutoTrade(requireContext())
                }
                handler.postDelayed(this, delay)
            }
        }, delay)
    }

    private fun attachTickerObserver() {
        tickersViewModel.fetchTickers()
        this.tickersViewModel.tickersLiveData.observe(viewLifecycleOwner, {
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
        });
    }

    private fun displayLinearLayout() {
        this.binding.xrpZarLinearLayoutCompat.visibility = View.VISIBLE
        this.binding.errorTextView.visibility = View.GONE
    }

    private fun displayErrorTextView() {
        this.binding.xrpZarLinearLayoutCompat.visibility = View.GONE
        this.binding.errorTextView.visibility = View.VISIBLE
    }

    private fun displayPrivacyPolicy(view: View) {
        val privacyPolicyAcceptance = SharedPrefsUtils[requireContext(), SharedPrefsUtils.PRIVACY_POLICY_ACCEPTANCE]
        if (privacyPolicyAcceptance == null) {
            val action = HomeFragmentDirections.actionHomeFragmentToPrivacyPolicyFragment()
            Navigation.findNavController(view).navigate(action)
        } else {
            setUserTrailingStartPrice()
            setUserTrailingStopPrice()
            setSupportPriceCounter()
            setResistancePriceCounter()
            setSmartTrendDetectorMargin()
        }
    }

    private fun setUserTrailingStartPrice() {
        val trailingStart = SharedPrefsUtils[requireContext(), SharedPrefsUtils.TRAILING_START]
        if (!trailingStart.isNullOrBlank()) {
            ConstantUtils.trailingStart = trailingStart.toInt()
        }
    }

    private fun setUserTrailingStopPrice() {
        val trailingStop = SharedPrefsUtils[requireContext(), SharedPrefsUtils.TRAILING_STOP]
        if (!trailingStop.isNullOrBlank()) {
            ConstantUtils.trailingStop = trailingStop.toInt()
        }
    }

    private fun setSupportPriceCounter() {
        val supportPriceCounter = SharedPrefsUtils[requireContext(), SharedPrefsUtils.SUPPORT_PRICE_COUNTER]
        if (!supportPriceCounter.isNullOrBlank()) {
            ConstantUtils.supportPriceCounter = supportPriceCounter.toInt()
        }
    }

    private fun setResistancePriceCounter() {
        val resistancePriceCounter = SharedPrefsUtils[requireContext(), SharedPrefsUtils.RESISTANCE_PRICE_COUNTER]
        if (!resistancePriceCounter.isNullOrBlank()) {
            ConstantUtils.resistancePriceCounter = resistancePriceCounter.toInt()
        }
    }

    private fun setSmartTrendDetectorMargin() {
        val smartTrendDetectorMargin = SharedPrefsUtils[requireContext(), SharedPrefsUtils.SMART_TREND_DETECTOR]
        if (!smartTrendDetectorMargin.isNullOrBlank()) {
            ConstantUtils.smartTrendDetectorMargin = smartTrendDetectorMargin.toInt()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeMessages(0)
    }
}