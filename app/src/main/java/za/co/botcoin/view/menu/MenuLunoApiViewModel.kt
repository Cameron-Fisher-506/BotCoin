package za.co.botcoin.view.menu

import android.app.Application
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService.LUNO_API_KEY_ID
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService.LUNO_API_SECRET_KEY
import za.co.botcoin.utils.services.sharePreferencesService.ISharedPreferencesService

class MenuLunoApiViewModel(
    application: Application,
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val sharedPreferencesService: ISharedPreferencesService
) : BaseViewModel(application) {
    fun displayApiKeySavedToast() {
        alertDialogService.makeToast(resourceManager.getString(R.string.menu_api_key_saved))
    }

    fun displayLunoApiCredentialsAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.menu_luno_api_credentials_luno_api)
            message = resourceManager.getString(R.string.menu_please_set_your_luno_api)
        })
    }

    fun saveLunoApiKeyId(keyId: String) = sharedPreferencesService.save(LUNO_API_KEY_ID, keyId)

    fun saveLunoApiSecretKey(secretKey: String) = sharedPreferencesService.save(LUNO_API_SECRET_KEY, secretKey)
}