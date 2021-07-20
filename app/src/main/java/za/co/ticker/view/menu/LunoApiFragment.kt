package za.co.ticker.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import za.co.ticker.R
import za.co.ticker.databinding.LunoApiFragmentBinding
import za.co.ticker.utils.ConstantUtils
import za.co.ticker.utils.GeneralUtils
import za.co.ticker.utils.SharedPrefsUtils

class LunoApiFragment : Fragment(R.layout.luno_api_fragment) {
    private lateinit var binding: LunoApiFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = LunoApiFragmentBinding.bind(view)

        ConstantUtils.USER_KEY_ID?.let { this.binding.keyIdEditText.setText(it) }
        ConstantUtils.USER_SECRET_KEY?.let { this.binding.secretKeyEditText.setText(it) }
        setBtnSaveListener()
    }

    private fun setBtnSaveListener() {
        this.binding.saveButton.setOnClickListener {
            val keyID = this.binding.keyIdEditText.text.toString()
            val secretKey = this.binding.secretKeyEditText.text.toString()

            if (keyID.isNotBlank() && secretKey.isNotBlank()) {
                ConstantUtils.USER_KEY_ID = keyID
                ConstantUtils.USER_SECRET_KEY = secretKey

                context?.let { context ->
                    SharedPrefsUtils.save(context, SharedPrefsUtils.LUNO_API_KEY_ID, keyID)
                    SharedPrefsUtils.save(context, SharedPrefsUtils.LUNO_API_SECRET_KEY, secretKey)
                }
                GeneralUtils.makeToast(activity, "API Key Saved!")
            } else {
                GeneralUtils.createAlertDialog(activity, "Luno API Credentials (Luno API)", "Please set your Luno API credentials in order to use BotCoin!", false).show()
            }
        }
    }
}