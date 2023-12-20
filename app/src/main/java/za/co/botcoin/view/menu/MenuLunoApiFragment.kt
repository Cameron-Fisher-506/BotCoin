package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.example.composecorelib.buttons.ButtonView
import com.example.composecorelib.buttons.CustomInputViewCompose
import com.example.composecorelib.informational.InformationViewCompose
import za.co.botcoin.utils.ConstantUtils

class MenuLunoApiFragment : MenuBaseFragment() {
    private val menuLunoApiViewModel by viewModels<MenuLunoApiViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = "Luno API") },
                        colors = topAppBarColors(
                            containerColor = Color(0xFF1976D2),
                            titleContentColor = Color(0xFFFFFFFF),
                        )
                    )
                }) {
                    Surface(Modifier.padding(it)) {
                        Column {
                            InformationViewCompose("Information") {
                                menuLunoApiViewModel.displayLunoApiCredentialsInformationAlertDialog()
                            }
                            CustomInputViewCompose("Key ID", ConstantUtils.USER_KEY_ID)
                            CustomInputViewCompose("Secret Key", ConstantUtils.USER_SECRET_KEY)
                            ButtonView("Save") {
                                saveData()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveData() {
        val keyId = ""
        val secretKey = ""

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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun MenuLunoApiScreen() {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Luno API") },
            colors = topAppBarColors(
                containerColor = Color(0xFF1976D2),
                titleContentColor = Color(0xFFFFFFFF),
            )
        )
    }) {
        Surface(Modifier.padding(it)) {
            Column {
                InformationViewCompose("Information") {

                }
                CustomInputViewCompose("Key ID", ConstantUtils.USER_KEY_ID)
                CustomInputViewCompose("Secret Key", ConstantUtils.USER_SECRET_KEY)
                ButtonView("Save") {
                    
                }
            }
        }
    }
}