package za.co.botcoin.view.settings

import androidx.lifecycle.ViewModel
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService.AUTO_TRADE_PREF
import za.co.botcoin.utils.services.sharePreferencesService.ISharedPreferencesService

class AutoTradeViewModel(
    private val resourceManager: IResourceManager,
    private val alertDialogService: IAlertDialogService,
    private val sharedPreferencesService: ISharedPreferencesService
) : ViewModel() {

    fun displayAutoTradeAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.auto_trade)
            message = resourceManager.getString(R.string.auto_trade_disclaimer)
        })
    }

    fun displayLunoApiCredentialsAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.auto_trade_luno_api_credentials)
            message = resourceManager.getString(R.string.auto_trade_please_set_your_luno_api)
        })
    }

    fun saveAutoTradePref(isChecked: Boolean) = sharedPreferencesService.save(AUTO_TRADE_PREF, isChecked.toString())

    fun getAutoTradePref(): String? = sharedPreferencesService[AUTO_TRADE_PREF]
}