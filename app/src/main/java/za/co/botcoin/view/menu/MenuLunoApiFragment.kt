package za.co.botcoin.view.menu

import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.example.composecorelib.buttons.CustomInputViewCompose
import com.example.composecorelib.informational.InformationViewCompose
import za.co.botcoin.R

class MenuLunoApiFragment : MenuBaseFragment(R.layout.luno_api_fragment) {
    private val menuLunoApiViewModel by viewModels<MenuLunoApiViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding = LunoApiFragmentBinding.bind(view)

        setUpViews()
        setUpOnClickListeners()
    }

    private fun setUpViews() {
       /* binding.keyIdCustomInputView.setText(ConstantUtils.USER_KEY_ID)
        binding.secretKeyCustomInputView.setText(ConstantUtils.USER_SECRET_KEY)*/
    }

    private fun setUpOnClickListeners() {
        /*this.binding.saveButton.setOnClickListener {
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
        }*/
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun MenuLunoApiScreen() {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Luno API") },
            colors = topAppBarColors(
                containerColor = Color(0xFF1976D2),
                titleContentColor = Color(0xFFFFFFFF),
            ))
    }) {
        Surface(Modifier.padding(it)) {
            Column {
                InformationViewCompose("Information")
                CustomInputViewCompose("Key ID")
                CustomInputViewCompose("Secret Key")
            }
        }
    }
}