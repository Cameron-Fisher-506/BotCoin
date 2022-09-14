package za.co.botcoin.view.wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import za.co.botcoin.R
import za.co.botcoin.databinding.WithdrawFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.isApiKeySet

class WithdrawFragment : WalletBaseFragment(R.layout.withdraw_fragment) {
    private lateinit var binding: WithdrawFragmentBinding
    private val withdrawViewModel by viewModels<WithdrawViewModel>(factoryProducer = { walletActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = WithdrawFragmentBinding.bind(view)

        addWithdrawListener()
    }

    private fun addWithdrawListener() {
        this.binding.withdrawButton.setOnClickListener {
            val amount: String = this.binding.amountEditText.text.toString()
            val beneficiaryId: String = this.binding.amountEditText.text.toString()

            if (withdrawViewModel.isAmountNotEmptyAndNotZero(amount) && beneficiaryId.isNotBlank()) {
                if (isApiKeySet(context)) {
                    withdrawViewModel.withdrawal("ZAR_EFT", amount, beneficiaryId)
                    attachWithdrawalObserver()
                } else {
                    walletViewModel.displayLunoApiCredentialsAlertDialog()
                }
            } else {
                withdrawViewModel.displayWithdrawalAlertDialog()
            }
        }
    }

    private fun attachWithdrawalObserver() {
        this.withdrawViewModel.withdrawalResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    displayWithdrawOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { withdrawal -> withdrawViewModel.displayAmountWithdrewNotification(withdrawal.amount) }
                    } else {
                        withdrawViewModel.displayWithdrawalFailedNotification()
                    }

                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayWithdrawOptions()
                    withdrawViewModel.displayWithdrawalFailedNotification()
                }
                Status.LOADING -> {
                    walletActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
    }

    private fun hideAllViews() {
        this.binding.withdrawButton.visibility = View.GONE
        this.binding.amountEditText.visibility = View.GONE
        this.binding.beneficiaryIdEditText.visibility = View.GONE
        this.binding.withdrawTextView.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
    }

    private fun displayWithdrawOptions() {
        hideAllViews()
        this.binding.withdrawButton.visibility = View.VISIBLE
        this.binding.amountEditText.visibility = View.VISIBLE
        this.binding.beneficiaryIdEditText.visibility = View.VISIBLE
        this.binding.withdrawTextView.visibility = View.VISIBLE
    }

    private fun displayErrorTextView() {
        hideAllViews()
        this.binding.errorTextView.visibility = View.VISIBLE
    }

    private fun displayProgressBar() {
        hideAllViews()
    }
}