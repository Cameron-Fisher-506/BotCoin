package za.co.botcoin.view.wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.WalletFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.*
import za.co.botcoin.view.wallet.WalletViewModel.Companion.XRP
import za.co.botcoin.view.wallet.WalletViewModel.Companion.ZAR

class WalletFragment : WalletBaseFragment(R.layout.wallet_fragment) {
    private lateinit var binding: WalletFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = WalletFragmentBinding.bind(view)

        setUpOnClickListeners()
        if (GeneralUtils.isApiKeySet(context)) {
            attachBalanceObserver()
        } else {
            walletViewModel.displayLunoApiCredentialsAlertDialog()
        }
    }

    private fun attachBalanceObserver() {
        this.walletViewModel.fetchBalances()
        this.walletViewModel.balancesResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        displayWalletOptions()
                        data.map { balance ->
                            when {
                                balance.asset.equals(XRP, true) -> this.binding.xrpOptionActionView.setText(getString(R.string.XRP, balance.balance))
                                balance.asset.equals(ZAR, true) -> this.binding.zarOptionActionView.setText(getString(R.string.ZAR, balance.balance))
                            }
                        }
                    } else {
                        displayErrorMessage()
                    }
                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayErrorMessage()
                }
                Status.LOADING -> {
                    walletActivity.displayProgressBar()
                    hideErrorMessage()
                    hideWalletOptions()
                }
            }
        }
    }

    private fun displayWalletOptions() {
        hideErrorMessage()
        this.binding.walletGroup.visibility = View.VISIBLE
    }

    private fun hideWalletOptions() {
        this.binding.walletGroup.visibility = View.GONE
    }

    private fun displayErrorMessage() {
        hideWalletOptions()
        this.binding.errorTextView.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        this.binding.errorTextView.visibility = View.GONE
    }

    private fun setUpOnClickListeners() {
        this.binding.zarOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(WalletFragmentDirections.actionWalletFragmentToWithdrawFragment())
        }

        this.binding.xrpOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(WalletFragmentDirections.actionWalletFragmentToWalletMenuFragment(XRP))
        }
    }
}