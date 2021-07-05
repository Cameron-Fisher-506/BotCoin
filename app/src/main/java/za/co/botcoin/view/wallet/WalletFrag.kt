package za.co.botcoin.view.wallet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.WalletFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.*
import za.co.botcoin.view.home.MainActivity

class WalletFrag : Fragment(R.layout.wallet_fragment) {
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
            GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false)?.show()
        }
    }

    private fun attachBalanceObserver() {
        this.walletViewModel.fetchBalances(true)
        this.walletViewModel.balancesLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        displayLinearLayouts()

                        data.map { balance ->
                            if (balance.asset == ConstantUtils.XRP) {
                                this.binding.txtXrp.append(balance.balance)
                            } else if (balance.asset == ConstantUtils.ZAR) {
                                this.binding.txtZar.append(balance.balance)
                            }
                        }
                    } else {
                        displayErrorTextView()
                    }
                }
                Status.ERROR -> { displayErrorTextView() }
                Status.LOADING -> { displayProgressBar() }
            }
        })
    }

    private fun displayLinearLayouts() {
        hideAllView()
        this.binding.linearLayoutZar.visibility = View.VISIBLE
        this.binding.linearLayoutXrp.visibility = View.VISIBLE
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
        this.binding.linearLayoutZar.visibility = View.GONE
        this.binding.linearLayoutXrp.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
        this.binding.progressBar.visibility = View.GONE
    }

    private fun addZarOptionListener() {
        this.binding.linearLayoutZar.setOnClickListener {
            val action = WalletFragDirections.actionWalletFragToWithdrawFrag()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addXrpOptionListener() {
        this.binding.linearLayoutXrp.setOnClickListener {
            val action = WalletFragDirections.actionWalletFragToWalletMenuFrag(ConstantUtils.XRP)
            Navigation.findNavController(it).navigate(action)
        }
    }
}