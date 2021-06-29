package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.ReceiveFragmentBinding
import za.co.botcoin.view.menu.LunoApiFrag
import za.co.botcoin.utils.ClipBoardUtils.copyToClipBoard
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.createQRCode
import za.co.botcoin.utils.GeneralUtils.getAuth
import za.co.botcoin.utils.GeneralUtils.getCurrentDateTime
import za.co.botcoin.utils.GeneralUtils.isApiKeySet
import za.co.botcoin.utils.StringUtils
import za.co.botcoin.utils.WSCallUtilsCallBack
import za.co.botcoin.utils.WSCallsUtils

class ReceiveFrag : Fragment(R.layout.receive_fragment), WSCallUtilsCallBack {
    private lateinit var binding: ReceiveFragmentBinding
    private val REQ_CODE_FUNDING_ADDRESS = 101

    private var address: String? = null
    private var qrCode: String? = null
    private var asset: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = ReceiveFragmentBinding.bind(view)

        asset = arguments!!.getString("asset")
        addBtnCopyListener()

        if (isApiKeySet(context)) {
            botCoinAccountDetails
        } else {
            createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false)!!.show()
            val lunoApiFrag = LunoApiFrag()
            //startFragment((activity as MainActivity?)!!.supportFragmentManager, lunoApiFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Luno API", true, false, true, null)
        }
    }

    private val botCoinAccountDetails: Unit
        private get() {
            WSCallsUtils.get(this, REQ_CODE_FUNDING_ADDRESS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_FUNDING_ADDRESS + "?asset=" + asset, getAuth(ConstantUtils.KEY_ID, ConstantUtils.SECRET_KEY))
        }

    private fun addBtnCopyListener() {
        this.binding.btnCopy.setOnClickListener { copyToClipBoard(activity!!, address) }
    }

    override fun taskCompleted(response: String?, reqCode: Int) {
        if (response != null) {
            if (reqCode == REQ_CODE_FUNDING_ADDRESS) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("error")) {
                        createAlertDialog(activity!!.applicationContext, "Oops!", jsonObject.getString("error"), false)
                    } else {
                        address = jsonObject.getString("address")
                        this.binding.edTxtAddress.setText(address)
                        qrCode = jsonObject.getString("qr_code_uri")
                        this.binding.imgQRAddress.setImageBitmap(createQRCode(qrCode, this.binding.imgQRAddress.width, this.binding.imgQRAddress.height))
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
}