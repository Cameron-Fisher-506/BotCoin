package za.co.ticker.view.wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import za.co.ticker.R
import za.co.ticker.databinding.WalletFragmentBinding
import za.co.ticker.enum.Status
import za.co.ticker.utils.*

class WalletFragment : Fragment(R.layout.wallet_fragment) {
    private lateinit var binding: WalletFragmentBinding
    private lateinit var walletViewModel: WalletViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = WalletFragmentBinding.bind(view)

        this.walletViewModel = ViewModelProviders.of(this).get(WalletViewModel::class.java)

        addZarOptionListener()
        addXrpOptionListener()
        if (GeneralUtils.isApiKeySet(context)) {
            attachBalanceObserver()
        } else {
            GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false).show()
        }
    }

    private fun attachBalanceObserver() {
        this.walletViewModel.fetchBalances()
        this.walletViewModel.balancesLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        displayLinearLayouts()

                        data.map { balance ->
                            if (balance.asset == ConstantUtils.XRP) {
                                this.binding.xrpTextView.append(balance.balance)
                            } else if (balance.asset == ConstantUtils.ZAR) {
                                this.binding.zarTextView.append(balance.balance)
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
                    displayProgressBar()
                }
            }
        })
    }

    private fun displayLinearLayouts() {
        hideAllView()
        this.binding.zarLinearLayoutCompat.visibility = View.VISIBLE
        this.binding.xrpLinearLayoutCompat.visibility = View.VISIBLE
    }

    private fun displayErrorTextView() {
        hideAllView()
        this.binding.errorTextView.visibility = View.VISIBLE
    }

    private fun displayProgressBar() {
        hideAllView()
        this.binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideAllView() {
        this.binding.zarLinearLayoutCompat.visibility = View.GONE
        this.binding.xrpLinearLayoutCompat.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
        this.binding.progressBar.visibility = View.GONE
    }

    private fun addZarOptionListener() {
        this.binding.zarLinearLayoutCompat.setOnClickListener {
            val action = WalletFragmentDirections.actionWalletFragmentToWithdrawFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addXrpOptionListener() {
        this.binding.xrpLinearLayoutCompat.setOnClickListener {
            val action = WalletFragmentDirections.actionWalletFragmentToWalletMenuFragment(ConstantUtils.XRP)
            Navigation.findNavController(it).navigate(action)
        }
    }
}