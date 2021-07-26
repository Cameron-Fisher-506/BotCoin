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
import za.co.botcoin.utils.ConstantUtils
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
            setUserPulloutBidPrice()
            //GeneralUtils.runAutoTrade(requireContext())
        }
    }

    private fun setUserPulloutBidPrice() {
        val trailingStop = SharedPrefsUtils[requireContext(), SharedPrefsUtils.TRAILING_STOP]
        if (trailingStop != null) {
            ConstantUtils.trailingStop = trailingStop.toInt()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeMessages(0)
    }
}