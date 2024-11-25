package za.co.botcoin.view.settings

import androidx.lifecycle.ViewModel
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService

class AutoTradeSwitchViewModel(
    private val resourceManager: IResourceManager,
    private val alertDialogService: IAlertDialogService,
    private val sharedPreferencesService: ISharedPreferencesService
) : ViewModel() {

    fun displayAutoTradeDisclaimerAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.auto_trade)
            message = resourceManager.getString(R.string.auto_trade_disclaimer)
        })
    }

    fun displayAutoTradeInstructionsAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.auto_trade)
            message = resourceManager.getString(R.string.auto_trade_instructions)
        })
    }

    fun displayLunoApiCredentialsAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.auto_trade_luno_api_credentials)
            message = resourceManager.getString(R.string.auto_trade_please_set_your_luno_api)
        })
    }

    fun saveAutoTradePref(isChecked: Boolean) = sharedPreferencesService.save(
        BaseSharedPreferencesService.AUTO_TRADE_PREF, isChecked.toString())

    fun getAutoTradePref(): String? = sharedPreferencesService[BaseSharedPreferencesService.AUTO_TRADE_PREF]
}