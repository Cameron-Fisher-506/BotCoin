package za.co.botcoin.view.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.AlertDialogService

class AutoTradeViewModel(private var alertDialogService: AlertDialogService): ViewModel() {

    fun displayAutoTradeAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = "Auto Trade"
            message = "Testing"

        })
    }
}