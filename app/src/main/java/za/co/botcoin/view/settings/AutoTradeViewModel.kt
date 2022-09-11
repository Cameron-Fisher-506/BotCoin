package za.co.botcoin.view.settings

import androidx.lifecycle.ViewModel
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService

class AutoTradeViewModel(
    private val resourceManager: IResourceManager,
    private var alertDialogService: IAlertDialogService
) : ViewModel() {

    fun displayAutoTradeAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.auto_trade)
            message = resourceManager.getString(R.string.auto_trade_disclaimer)
        })
    }
}