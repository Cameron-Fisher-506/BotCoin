package za.co.botcoin.view.wallet

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import za.co.botcoin.R
import za.co.botcoin.databinding.WithdrawFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.isApiKeySet

class WithdrawFrag : Fragment(R.layout.withdraw_fragment) {
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
                    createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false)!!.show()
                }
            } else {
                createAlertDialog(activity, "Withdrawal", "Please provide an amount more than 0 and a valid beneficiary ID!", false)!!.show()
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
                        data.map { withdrawal ->  notify("Withdrew " + withdrawal.amount + " Rands.", "") }
                    } else {
                        notify("Withdrawal Failed", "")
                    }

                }
                Status.ERROR -> {
                    displayWithdrawOptions()
                    notify("Withdrawal Failed", "")
                }
                Status.LOADING -> { displayProgressBar() }
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

    private fun notify(title: String?, message: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val notification = Notification.Builder(context)
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .addAction(R.drawable.luno_icon, "Action 1", pendingIntent)
                    .setContentIntent(pendingIntent).notification
            notification.flags = Notification.FLAG_AUTO_CANCEL
            val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val notification = Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .build()
            val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }
    }
}