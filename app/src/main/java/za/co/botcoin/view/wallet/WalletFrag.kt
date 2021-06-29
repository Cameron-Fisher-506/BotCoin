package za.co.botcoin.view.wallet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import org.json.JSONObject
import za.co.botcoin.view.home.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.WalletFragmentBinding
import za.co.botcoin.utils.*

class WalletFrag : Fragment(R.layout.wallet_fragment), WSCallUtilsCallBack {
    private lateinit var binding: WalletFragmentBinding
    private val BALANCE_REQ_CODE = 101

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = WalletFragmentBinding.bind(view)

        addZarOptionListener()
        addXrpOptionListener()
        if (GeneralUtils.isApiKeySet(context)) {
            //Get ZAR and XRP balance
            WSCallsUtils.get(this, BALANCE_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_BALANCE, GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID ?: "", ConstantUtils.USER_SECRET_KEY ?: ""))
        } else {
            GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false)?.show()
        }
    }

    private fun addZarOptionListener() {
        this.binding.linearLayoutZar.setOnClickListener {
            val action = WalletFragDirections.actionWalletFragToWithdrawFrag()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addXrpOptionListener() {
        this.binding.linearLayoutXrp.setOnClickListener(View.OnClickListener {
            val action = WalletFragDirections.actionWalletFragToWalletMenuFrag(ConstantUtils.XRP)
            Navigation.findNavController(it).navigate(action)
        })
    }

    override fun taskCompleted(response: String?, reqCode: Int) {
        if (response != null) {
            if (reqCode == BALANCE_REQ_CODE) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("balance")) {
                        try {
                            val jsonArrayBalance = jsonObject.getJSONArray("balance")
                            if (jsonArrayBalance != null && jsonArrayBalance.length() > 0) {
                                for (i in 0 until jsonArrayBalance.length()) {
                                    val jsonObjectBalance = jsonArrayBalance.getJSONObject(i)
                                    val currency = jsonObjectBalance.getString("asset")
                                    val balance = jsonObjectBalance.getString("balance")
                                    val reserved = jsonObjectBalance.getString("reserved")
                                    if (currency == ConstantUtils.XRP) {
                                        this.binding.txtXrp.append(balance)
                                    } else if (currency == ConstantUtils.ZAR) {
                                        this.binding.txtZar.append(balance)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                                    "Method: MainActivity - onCreate " +
                                    "URL: ${StringUtils.GLOBAL_LUNO_URL}/api/1/balance " +
                                    "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                        }
                    } else {
                        Log.e(ConstantUtils.BOTCOIN_TAG, "Error: No Response " +
                                "Method: MainActivity - onCreate " +
                                "URL: ${StringUtils.GLOBAL_LUNO_URL}/api/1/balance " +
                                "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                            "Method: MainActivity - onCreate " +
                            "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                }
            }
        } else {
            GeneralUtils.createAlertDialog(activity as MainActivity?, "No Signal", "Please check your network connection!", false)
        }
    }
}