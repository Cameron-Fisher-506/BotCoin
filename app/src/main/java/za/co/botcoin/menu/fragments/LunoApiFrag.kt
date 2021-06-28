package za.co.botcoin.menu.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.LunoApiFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPreferencesUtils

class LunoApiFrag : Fragment(R.layout.luno_api_fragment) {
    private lateinit var binding: LunoApiFragmentBinding

    companion object {
        const val FRAG_NUM = 4
        const val TITLE = "Luno API"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = LunoApiFragmentBinding.bind(view)

        ConstantUtils.USER_KEY_ID?.let { this.binding.edTxtKeyID.setText(it) }
        ConstantUtils.USER_SECRET_KEY?.let { this.binding.edTxtSecretKey.setText(it) }
        setBtnSaveListener()
    }

    private fun setBtnSaveListener() {
        this.binding.btnSave.setOnClickListener {
            val keyID = this.binding.edTxtKeyID.text.toString()
            val secretKey = this.binding.edTxtSecretKey.text.toString()

            if (keyID.isNotEmpty() && secretKey.isNotEmpty()) {
                ConstantUtils.USER_KEY_ID = keyID
                ConstantUtils.USER_SECRET_KEY = secretKey

                try {
                    val jsonObjectApiKey = JSONObject()
                    jsonObjectApiKey.put("keyID", keyID)
                    jsonObjectApiKey.put("secretKey", secretKey)

                    activity?.let { context -> SharedPreferencesUtils.save(context, SharedPreferencesUtils.LUNO_API_PREF, jsonObjectApiKey) }
                    GeneralUtils.makeToast(activity, "API Key Saved!")
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                            "Method: LunoApiFrag - onCreateView " +
                            "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                }
            } else {
                GeneralUtils.createAlertDialog(activity, "Luno API Credentials (Luno API)", "Please set your Luno API credentials in order to use BotCoin!", false)?.show()
            }
        }
    }
}