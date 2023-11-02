package za.co.botcoin.view.wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import za.co.botcoin.R
import za.co.botcoin.databinding.WithdrawFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.GeneralUtils.isApiKeySet
import za.co.botcoin.view.wallet.WalletWithdrawViewModel.Companion.ZAR_EFT

class WalletWithdrawFragment : WalletBaseFragment(R.layout.withdraw_fragment) {
    private lateinit var binding: WithdrawFragmentBinding
    private val withdrawViewModel by viewModels<WalletWithdrawViewModel>(factoryProducer = { walletActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WithdrawFragmentBinding.bind(view)
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.withdrawButton.setOnClickListener {
            if (withdrawViewModel.isAmountNotEmptyAndNotZero(binding.amountCustomInputView.getText()) && binding.beneficiaryIdCustomInputView.getText().isNotBlank()) {
                if (isApiKeySet(context)) {
                    submitWithdrawalAndObserve()
                } else {
                    walletViewModel.displayLunoApiCredentialsAlertDialog()
                }
            } else {
                withdrawViewModel.displayWithdrawalAlertDialog()
            }
        }
        binding.withdrawInformationView.setOnClickListener {
            withdrawViewModel.displayWithdrawalDescriptionAlertDialog()
        }
    }

    private fun submitWithdrawalAndObserve() {
        withdrawViewModel.withdrawal(ZAR_EFT, binding.amountCustomInputView.getText(), binding.beneficiaryIdCustomInputView.getText())
        withdrawViewModel.withdrawalResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    displayWalletWithdrawOptions()
                    if (!it.data.isNullOrEmpty()) {
                        it.data.map { withdrawal -> withdrawViewModel.displayAmountWithdrewNotification(withdrawal.amount) }
                    } else {
                        displayErrorMessage()
                        withdrawViewModel.displayWithdrawalFailedNotification()
                    }
                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayErrorMessage()
                    withdrawViewModel.displayWithdrawalFailedNotification()
                }
                Status.LOADING -> {
                    walletActivity.displayProgressBar()
                    hideWalletWithdrawOptions()
                    hideErrorMessage()
                }
            }
        }
    }

    private fun hideWalletWithdrawOptions() {
        this.binding.walletWithdrawGroup.visibility = View.GONE
    }

    private fun displayWalletWithdrawOptions() {
        hideErrorMessage()
        this.binding.walletWithdrawGroup.visibility = View.VISIBLE
    }

    private fun displayErrorMessage() {
        hideWalletWithdrawOptions()
        this.binding.errorTextView.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        this.binding.errorTextView.visibility = View.GONE
    }
}