package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import za.co.botcoin.R
import za.co.botcoin.databinding.LunoApiFragmentBinding
import za.co.botcoin.utils.ConstantUtils

class MenuLunoApiFragment : MenuBaseFragment(R.layout.luno_api_fragment) {
    private lateinit var binding: LunoApiFragmentBinding
    private val menuLunoApiViewModel by viewModels<MenuLunoApiViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LunoApiFragmentBinding.bind(view)

        setUpViews()
        setUpOnClickListeners()
    }

    private fun setUpViews() {
        binding.keyIdEditText.setText(ConstantUtils.USER_KEY_ID)
        binding.secretKeyEditText.setText(ConstantUtils.USER_SECRET_KEY)
    }

    private fun setUpOnClickListeners() {
        this.binding.saveButton.setOnClickListener {
            val keyId = this.binding.keyIdEditText.text.toString()
            val secretKey = this.binding.secretKeyEditText.text.toString()

            if (keyId.isNotBlank() && secretKey.isNotBlank()) {
                ConstantUtils.USER_KEY_ID = keyId
                ConstantUtils.USER_SECRET_KEY = secretKey

                menuLunoApiViewModel.saveLunoApiKeyId(keyId)
                menuLunoApiViewModel.saveLunoApiSecretKey(secretKey)
                menuLunoApiViewModel.displayApiKeySavedToast()
            } else {
                menuLunoApiViewModel.displayLunoApiCredentialsAlertDialog()
            }
        }
    }
}