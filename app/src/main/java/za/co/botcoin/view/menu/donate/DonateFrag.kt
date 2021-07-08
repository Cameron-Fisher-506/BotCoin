package za.co.botcoin.view.menu.donate

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.DonateFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.*
import za.co.botcoin.view.menu.LunoApiFrag

class DonateFrag : Fragment(R.layout.donate_fragment) {
    private lateinit var binding: DonateFragmentBinding
    private lateinit var donateViewModel: DonateViewModel

    private var asset: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = DonateFragmentBinding.bind(view)

        this.donateViewModel = ViewModelProviders.of(this).get(DonateViewModel::class.java)
        asset = arguments?.getString("asset") ?: ""

        wireUI()
        if (GeneralUtils.isApiKeySet(context)) {
            this.donateViewModel.receive(asset)
            attachReceiveObserver()
        } else {
            GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false)?.show()
            val action = DonateFragDirections.actionDonateFragToLunoApiFrag2()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun attachReceiveObserver() {
        this.donateViewModel.receiveLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    displayDonateOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        this.binding.edTxtAddress.setText(data.first().address)
                        /*if (tagValue != null) {
                            this.binding.edTxtTag.setText(data.first().)
                        } else {
                            this.binding.edTxtTag.visibility = View.INVISIBLE
                            this.binding.btnCopyTag.visibility = View.INVISIBLE
                        }*/
                        this.binding.imgQRAddress.setImageBitmap(GeneralUtils.createQRCode(data.first().qrCodeUri, this.binding.imgQRAddress.width, this.binding.imgQRAddress.height))
                    } else {
                        displayErrorTextView()
                    }
                }
                Status.ERROR -> { displayErrorTextView() }
                Status.LOADING -> { displayProgressBar() }
            }
        })
    }

    private fun attachSendObserver(amount: String, asset: String, address: String) {
        this.donateViewModel.sendLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    displayDonateOptions()
                    val data = it.data
                    if(!data.isNullOrEmpty()) {
                        data.map { response -> if (response.success) notify("Sent $amount $asset to $address.", response.withdrawalId) else notify("Send failed.", "")}
                    } else {
                        notify("Send failed.", "")
                    }
                }
                Status.ERROR -> {
                    displayDonateOptions()
                    notify("Send failed.", "")
                }
                Status.LOADING -> { displayProgressBar() }
            }
        })
    }

    private fun wireUI() {
        addBtnCopyListener()
        addBtnDonateListener()
        addBtnCopyTagListener()
    }

    private fun addBtnCopyListener() {
        this.binding.btnCopy.setOnClickListener { activity?.let { context -> ClipBoardUtils.copyToClipBoard(context, this.binding.edTxtAddress.text.toString()) } }
    }

    private fun addBtnCopyTagListener() {
        this.binding.btnCopyTag.setOnClickListener { activity?.let { context -> ClipBoardUtils.copyToClipBoard(context, this.binding.edTxtTag.text.toString()) } }
    }

    private fun addBtnDonateListener() {
        this.binding.btnDonate.setOnClickListener {
            val amount: String = this.binding.edTxtAmount.text.toString()
            val address: String = this.binding.edTxtAddress.text.toString()
            val destinationTag: String = this.binding.edTxtTag.text.toString()
            if (amount.isNotBlank()) {
                if (amount != "0") {
                    this.donateViewModel.send(amount, asset, address, destinationTag)
                    attachSendObserver(amount, asset, address)
                } else {
                    GeneralUtils.createAlertDialog(context, "Invalid amount entered!", "Please note that you cannot donate 0 $asset.", false)?.show()
                }
            } else {
                GeneralUtils.createAlertDialog(context, "No amount entered!", "Please enter the amount of $asset You would like to donate.", false)?.show()
            }
        }
    }

    private fun hideAllViews() {
        this.binding.btnCopy.visibility = View.GONE
        this.binding.btnCopyTag.visibility = View.GONE
        this.binding.btnDonate.visibility = View.GONE
        this.binding.edTxtAddress.visibility = View.GONE
        this.binding.edTxtAmount.visibility = View.GONE
        this.binding.edTxtTag.visibility = View.GONE
        this.binding.txtDonate.visibility = View.GONE
        this.binding.imgQRAddress.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
        this.binding.progressBar.visibility = View.GONE
    }

    private fun displayDonateOptions() {
        hideAllViews()
        this.binding.btnCopy.visibility = View.VISIBLE
        this.binding.btnCopyTag.visibility = View.VISIBLE
        this.binding.btnDonate.visibility = View.VISIBLE
        this.binding.edTxtAddress.visibility = View.VISIBLE
        this.binding.edTxtAmount.visibility = View.VISIBLE
        this.binding.edTxtTag.visibility = View.VISIBLE
        this.binding.txtDonate.visibility = View.VISIBLE
        this.binding.imgQRAddress.visibility = View.VISIBLE
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