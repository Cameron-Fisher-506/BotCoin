package za.co.botcoin.view.wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import za.co.botcoin.R
import za.co.botcoin.databinding.WithdrawFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.isApiKeySet

class WithdrawFragment : Fragment(R.layout.withdraw_fragment) {
    private lateinit var binding: WithdrawFragmentBinding
    private lateinit var withdrawalViewModel: WithdrawalViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = WithdrawFragmentBinding.bind(view)

        this.withdrawalViewModel = ViewModelProviders.of(this).get(WithdrawalViewModel::class.java)

        addWithdrawListener()
    }

    private fun addWithdrawListener() {
        this.binding.withdrawButton.setOnClickListener {
            val amount: String = this.binding.amountEditText.text.toString()
            val beneficiaryId: String = this.binding.amountEditText.text.toString()

            if (amount.isNotBlank() && amount != "0" && beneficiaryId.isNotBlank()) {
                if (isApiKeySet(context)) {
                    this.withdrawalViewModel.withdrawal("ZAR_EFT", amount, beneficiaryId)
                    attachWithdrawalObserver()
                } else {
                    createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false).show()
                }
            } else {
                createAlertDialog(activity, "Withdrawal", "Please provide an amount more than 0 and a valid beneficiary ID!", false).show()
            }
        }
    }

    private fun attachWithdrawalObserver() {
        this.withdrawalViewModel.withdrawalLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    displayWithdrawOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { withdrawal -> GeneralUtils.notify(context, "Withdrew " + withdrawal.amount + " Rands.", "") }
                    } else {
                        GeneralUtils.notify(context,"Withdrawal Failed", "")
                    }

                }
                Status.ERROR -> {
                    displayWithdrawOptions()
                    GeneralUtils.notify(context,"Withdrawal Failed", "")
                }
                Status.LOADING -> {
                    displayProgressBar()
                }
            }
        })
    }

    private fun hideAllViews() {
        this.binding.withdrawButton.visibility = View.GONE
        this.binding.amountEditText.visibility = View.GONE
        this.binding.beneficiaryIdEditText.visibility = View.GONE
        this.binding.withdrawTextView.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
        this.binding.progressBar.visibility = View.GONE
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
        this.binding.progressBar.visibility = View.VISIBLE
    }
}