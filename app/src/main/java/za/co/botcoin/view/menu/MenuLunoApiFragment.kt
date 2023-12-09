package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.example.corelib.buttons.CustomInputView
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
        binding.keyIdCustomInputView.setText(ConstantUtils.USER_KEY_ID)
        binding.secretKeyCustomInputView.setText(ConstantUtils.USER_SECRET_KEY)
    }

    private fun setUpOnClickListeners() {
        this.binding.saveButton.setOnClickListener {
            val keyId = this.binding.keyIdCustomInputView.getText()
            val secretKey = this.binding.secretKeyCustomInputView.getText()

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

        binding.lunoApiCredentialsInformationView.setOnClickListener {
            menuLunoApiViewModel.displayLunoApiCredentialsInformationAlertDialog()
        }
    }
}

@Composable
fun MenuLunoApiFragmentScreen() {
    CustomInputView()
}

@Preview(showBackground = true)
@Composable
fun ShowScreen() {
    MenuLunoApiFragmentScreen()
}