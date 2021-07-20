package za.co.ticker.view.home

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import za.co.ticker.R
import za.co.ticker.databinding.HomeFragmentBinding
import za.co.ticker.enum.Status
import za.co.ticker.utils.ConstantUtils
import za.co.ticker.utils.GeneralUtils

class HomeFragment : Fragment(R.layout.home_fragment) {
    private lateinit var binding: HomeFragmentBinding
    private lateinit var tickersViewModel: TickersViewModel

    private val handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = HomeFragmentBinding.bind(view)
        this.tickersViewModel = ViewModelProviders.of(this).get(TickersViewModel::class.java)
        attachTickerObserver()

        if (GeneralUtils.isApiKeySet(context)) {
            val delay: Long = 100000L
            handler.postDelayed(object : Runnable {
                override fun run() {
                    attachTickerObserver()
                    handler.postDelayed(this, delay)
                }
            }, delay)

        } else {
            GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false).show()
        }
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeMessages(0)
    }
}