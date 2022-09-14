package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.LunoApiFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService

class MenuLunoApiFragment : MenuBaseFragment(R.layout.luno_api_fragment) {
    private lateinit var binding: LunoApiFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = LunoApiFragmentBinding.bind(view)

        this.binding.keyIdEditText.setText(ConstantUtils.USER_KEY_ID)
        this.binding.secretKeyEditText.setText(ConstantUtils.USER_SECRET_KEY)
        setBtnSaveListener()
    }

    private fun setBtnSaveListener() {
        this.binding.saveButton.setOnClickListener {
            val keyID = this.binding.keyIdEditText.text.toString()
            val secretKey = this.binding.secretKeyEditText.text.toString()

            if (keyID.isNotBlank() && secretKey.isNotBlank()) {
                ConstantUtils.USER_KEY_ID = keyID
                ConstantUtils.USER_SECRET_KEY = secretKey

                BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.LUNO_API_KEY_ID, keyID)
                BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.LUNO_API_SECRET_KEY, secretKey)
                GeneralUtils.makeToast(activity, "API Key Saved!")
            } else {
                GeneralUtils.createAlertDialog(activity, "Luno API Credentials (Luno API)", "Please set your Luno API credentials in order to use BotCoin!", false).show()
            }
        }
    }
}