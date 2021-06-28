package za.co.botcoin.wallet.fragments

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
import za.co.botcoin.databinding.WithdrawFragmentBinding
import za.co.botcoin.utils.*
import za.co.botcoin.utils.GeneralUtils.buildWithdrawal
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.getAuth
import za.co.botcoin.utils.GeneralUtils.isApiKeySet

class WithdrawFrag : Fragment(R.layout.withdraw_fragment), WSCallUtilsCallBack {
    private lateinit var binding: WithdrawFragmentBinding
    private val REQ_CODE_WITHDRAW = 101

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = WithdrawFragmentBinding.bind(view)

        addWithdrawListener(view)
    }

    private fun addWithdrawListener(view: View) {
        this.binding.btnWithdraw.setOnClickListener(View.OnClickListener {
            if (this.binding.edTxtAmount.text.toString() != "" && this.binding.edTxtAmount.text.toString() != "0" && this.binding.edTxtBeneficiaryId.text.toString() != "") {
                if (isApiKeySet(context)) {
                    withdrawal(this.binding.edTxtAmount.text.toString(), this.binding.edTxtBeneficiaryId.text.toString())
                } else {
                    createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false)!!.show()
                }
            } else {
                createAlertDialog(activity, "Withdrawal", "Please provide an amount more than 0 and a valid beneficiary ID!", false)!!.show()
            }
        })
    }

    private fun withdrawal(amount: String, beneficiaryId: String) {
        WSCallsUtils.post(this, REQ_CODE_WITHDRAW, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_WITHDRAWALS + buildWithdrawal(amount, beneficiaryId), "", getAuth(ConstantUtils.USER_KEY_ID!!, ConstantUtils.USER_SECRET_KEY!!))
    }

    override fun taskCompleted(response: String?, reqCode: Int) {
        if (response != null) {
            if (reqCode == REQ_CODE_WITHDRAW) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null) {
                        notify("Withdrew " + this.binding.edTxtAmount.text.toString() + " Rands.", jsonObject.toString())
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                            "Method: DonateFrag - taskCompleted " +
                            "Request Code: $reqCode " +
                            "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                }
            }
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