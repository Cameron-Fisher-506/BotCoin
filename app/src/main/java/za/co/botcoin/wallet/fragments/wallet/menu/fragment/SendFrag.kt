package za.co.botcoin.wallet.fragments.wallet.menu.fragment

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
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.SendFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils.buildSend
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.getAuth
import za.co.botcoin.utils.GeneralUtils.getCurrentDateTime
import za.co.botcoin.utils.StringUtils
import za.co.botcoin.utils.WSCallUtilsCallBack
import za.co.botcoin.utils.WSCallsUtils

class SendFrag : Fragment(R.layout.send_fragment), WSCallUtilsCallBack {
    private lateinit var binding: SendFragmentBinding
    private val REQ_CODE_SEND = 101
    private var asset: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = SendFragmentBinding.bind(view)

        asset = arguments!!.getString("asset")
        addBtnSend()
    }

    private fun addBtnSend() {
        this.binding.btnSend.setOnClickListener(View.OnClickListener {
            if (this.binding.edTxtAmount.text.toString() != "" && this.binding.edTxtAddress.text.toString() != "") {
                if (this.binding.edTxtAmount.text.toString() != "0") {
                    send(this.binding.edTxtAmount.text.toString(), this.binding.edTxtAddress.text.toString(), if (this.binding.edTxtTag.text.toString() == "") null else this.binding.edTxtTag.text.toString())
                } else {
                    createAlertDialog(context, "Invalid amount entered!", "Please note that you cannot send 0 $asset.", false)!!.show()
                }
            } else {
                createAlertDialog(context, "Send", "Please enter the amount of $asset You would like to send. Please enter a valid recipient account address and tag.", false)!!.show()
            }
        })
    }

    private fun send(amount: String, address: String, tag: String?) {
        WSCallsUtils.post(this, REQ_CODE_SEND, StringUtils.GLOBAL_LUNO_URL + buildSend(amount, asset!!, address, tag), "", getAuth(ConstantUtils.USER_KEY_ID!!, ConstantUtils.USER_SECRET_KEY!!))
    }

    override fun taskCompleted(response: String?, reqCode: Int) {
        if (response != null) {
            if (reqCode == REQ_CODE_SEND) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null) {
                        notify("Sent " + this.binding.edTxtAmount.text.toString() + " " + asset + " to " + this.binding.edTxtAddress + ".", jsonObject.toString())
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                            "Method: DonateFrag - taskCompleted " +
                            "Request Code: $reqCode " +
                            "CreatedTime: ${getCurrentDateTime()}")
                }
            }
        } else {
        }
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