package za.co.botcoin.menu.fragments.donate.menu.fragment

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import org.json.JSONObject
import za.co.botcoin.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.DonateFragmentBinding
import za.co.botcoin.menu.fragments.LunoApiFrag
import za.co.botcoin.utils.*

class DonateFrag : Fragment(R.layout.donate_fragment), WSCallUtilsCallBack {
    private lateinit var binding: DonateFragmentBinding

    private val REQ_CODE_FUNDING_ADDRESS = 101
    private val REQ_CODE_SEND = 102

    private var address: String? = null
    private var tagValue: String? = null
    private var qrCode: String? = null
    private var asset: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = DonateFragmentBinding.bind(view)

        initUI(view)
        if (GeneralUtils.isApiKeySet(context)) {
            botCoinAccountDetails
        } else {
            GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false).show()
            val lunoApiFrag = LunoApiFrag()
            FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, lunoApiFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Luno API", true, false, true, null)
        }
    }

    private fun initUI(view: View) {
        asset = arguments!!.getString("asset")

        this.binding.imgQRAddress.visibility = View.GONE
        this.binding.edTxtAmount.visibility = View.GONE
        this.binding.btnDonate.visibility = View.GONE

        address = null
        tagValue = null
        addBtnCopyListener(view.findViewById(R.id.btnCopy))
        addBtnDonateListener(view.findViewById(R.id.btnDonate))
        addBtnCopyTagListener(view.findViewById(R.id.btnCopyTag))
    }

    private val botCoinAccountDetails: Unit
        private get() {
            WSCallsUtils.get(this, REQ_CODE_FUNDING_ADDRESS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_FUNDING_ADDRESS + "?asset=" + asset, GeneralUtils.getAuth(ConstantUtils.KEY_ID, ConstantUtils.SECRET_KEY))
        }

    private fun addBtnCopyListener(view: View) {
        this.binding.btnCopy.setOnClickListener { ClipBoardUtils.copyToClipBoard(activity, address) }
    }

    private fun addBtnCopyTagListener(view: View) {
        this.binding.btnCopyTag.setOnClickListener { ClipBoardUtils.copyToClipBoard(activity, tagValue) }
    }

    private fun addBtnDonateListener(view: View) {
        this.binding.btnDonate.setOnClickListener {
            if (this.binding.edTxtAmount.text != null && this.binding.edTxtAmount.text.toString() != "") {
                if (this.binding.edTxtAmount.text.toString() != "0") {
                    send(this.binding.edTxtAmount.text.toString(), address, tagValue)
                } else {
                    GeneralUtils.createAlertDialog(context, "Invalid amount entered!", "Please note that you cannot donate 0 $asset.", false).show()
                }
            } else {
                GeneralUtils.createAlertDialog(context, "No amount entered!", "Please enter the amount of $asset You would like to donate.", false).show()
            }
        }
    }

    private fun send(amount: String, address: String?, tagValue: String?) {
        WSCallsUtils.post(this, REQ_CODE_SEND, StringUtils.GLOBAL_LUNO_URL + GeneralUtils.buildSend(amount, asset, address, tagValue), "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
    }

    override fun taskCompleted(response: String, reqCode: Int) {
        if (response != null) {
            if (reqCode == REQ_CODE_FUNDING_ADDRESS) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("error")) {
                        GeneralUtils.createAlertDialog(activity!!.applicationContext, "Oops!", jsonObject.getString("error"), false)
                    } else {
                        val address_meta = jsonObject.getJSONArray("address_meta")
                        if (address_meta != null && address_meta.length() > 0) {
                            for (i in 0 until address_meta.length()) {
                                val jsonObjectAddressMeta = address_meta.getJSONObject(i)
                                if (jsonObjectAddressMeta.getString("label") == "Address") {
                                    address = jsonObjectAddressMeta.getString("value")
                                }
                                if (jsonObjectAddressMeta.getString("label") == asset + " Tag") {
                                    tagValue = jsonObjectAddressMeta.getString("value")
                                }
                            }
                        }
                        this.binding.edTxtAddress.setText(address)
                        if (tagValue != null) {
                            this.binding.edTxtTag.setText(tagValue)
                        } else {
                            this.binding.edTxtTag.visibility = View.INVISIBLE
                            this.binding.btnCopyTag.visibility = View.INVISIBLE
                        }
                        qrCode = jsonObject.getString("qr_code_uri")
                        this.binding.imgQRAddress.setImageBitmap(GeneralUtils.createQRCode(qrCode, this.binding.imgQRAddress.width, this.binding.imgQRAddress.height))
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                            "Method: DonateFrag - taskCompleted " +
                            "Request Code: $reqCode " +
                            "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                }
            }
            if (reqCode == REQ_CODE_SEND) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null) {
                        notify("Donated " + this.binding.edTxtAmount.text.toString() + " " + asset + ".", jsonObject.toString())
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                            "Method: DonateFrag - taskCompleted " +
                            "Request Code: $reqCode " +
                            "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
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