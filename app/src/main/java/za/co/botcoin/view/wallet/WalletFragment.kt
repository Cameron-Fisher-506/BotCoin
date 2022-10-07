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

class WalletFragment : WalletBaseFragment(R.layout.wallet_fragment) {
    private lateinit var binding: WalletFragmentBinding

    companion object {
        private const val XRP = "XRP"
        private const val ZAR = "ZAR"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = WalletFragmentBinding.bind(view)

        addZarOptionListener()
        addXrpOptionListener()
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
                        displayLinearLayouts()
                        data.map { balance ->
                            when {
                                balance.asset.equals(XRP, true) -> this.binding.xrpTextView.append(balance.balance)
                                balance.asset.equals(ZAR, true) -> this.binding.zarTextView.append(balance.balance)
                            }
                        }
                    } else {
                        displayErrorTextView()
                    }
                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayErrorTextView()
                }
                Status.LOADING -> {
                    walletActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
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
    }

    private fun hideAllView() {
        this.binding.zarLinearLayoutCompat.visibility = View.GONE
        this.binding.xrpLinearLayoutCompat.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
    }

    private fun addZarOptionListener() {
        this.binding.zarLinearLayoutCompat.setOnClickListener {
            val action = WalletFragmentDirections.actionWalletFragmentToWithdrawFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addXrpOptionListener() {
        this.binding.xrpLinearLayoutCompat.setOnClickListener {
            val action = WalletFragmentDirections.actionWalletFragmentToWalletMenuFragment(XRP)
            Navigation.findNavController(it).navigate(action)
        }
    }
}