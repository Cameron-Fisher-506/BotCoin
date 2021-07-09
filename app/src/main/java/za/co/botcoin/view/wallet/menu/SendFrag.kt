package za.co.botcoin.view.wallet.menu

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
import za.co.botcoin.databinding.SendFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.view.wallet.WithdrawalViewModel

class SendFrag : Fragment(R.layout.send_fragment) {
    private lateinit var binding: SendFragmentBinding
    private lateinit var withdrawalViewModel: WithdrawalViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = SendFragmentBinding.bind(view)

        this.withdrawalViewModel = ViewModelProviders.of(this).get(WithdrawalViewModel::class.java)

        arguments?.let {
            addBtnSend(it.getString("asset") ?: "")
        }
    }

    private fun addBtnSend(asset: String) {
        this.binding.btnSend.setOnClickListener {
            val amount = this.binding.edTxtAmount.text.toString()
            val address = this.binding.edTxtAddress.text.toString()
            val destinationTag = this.binding.edTxtTag.text.toString()
            if (amount.isNotBlank() && address.isNotBlank()) {
                if (amount != "0") {
                    this.withdrawalViewModel.send(amount, asset, address, destinationTag)
                    attachSendObserver(amount, asset, address)
                } else {
                    createAlertDialog(context, "Invalid amount entered!", "Please note that you cannot send 0 $asset.", false)!!.show()
                }
            } else {
                createAlertDialog(context, "Send", "Please enter the amount of $asset You would like to send. Please enter a valid recipient account address and tag.", false)!!.show()
            }
        }
    }

    private fun attachSendObserver(amount: String, asset: String, address: String) {
        this.withdrawalViewModel.sendLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    displaySendOptions()
                    val data = it.data
                    if(!data.isNullOrEmpty()) {
                        data.map { response -> if (response.success) notify("Sent $amount $asset to $address.", response.withdrawalId) else notify("Send failed.", "")}
                    } else {
                        notify("Send failed.", "")
                    }
                }
                Status.ERROR -> {
                    displaySendOptions()
                    notify("Send failed.", "")
                }
                Status.LOADING -> { displayProgressBar() }
            }
        })
    }

    private fun hideAllViews() {
        this.binding.txtSend.visibility = View.GONE
        this.binding.btnSend.visibility = View.GONE
        this.binding.edTxtAddress.visibility = View.GONE
        this.binding.edTxtAmount.visibility = View.GONE
        this.binding.edTxtTag.visibility = View.GONE
        this.binding.progressBar.visibility = View.GONE
    }

    private fun displayProgressBar() {
        hideAllViews()
        this.binding.progressBar.visibility = View.VISIBLE
    }

    private fun displaySendOptions() {
        hideAllViews()
        this.binding.txtSend.visibility = View.VISIBLE
        this.binding.btnSend.visibility = View.VISIBLE
        this.binding.edTxtAddress.visibility = View.VISIBLE
        this.binding.edTxtAmount.visibility = View.VISIBLE
        this.binding.edTxtTag.visibility = View.VISIBLE
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