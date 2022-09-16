package za.co.botcoin.utils.services.alertDialogService

import android.content.Context

class AlertDialogService(private val context: Context): IAlertDialogService {
    override fun showAlertDialog(alertDialogProperties: AlertDialogProperties) {
        BaseAlertDialog.showAlertDialog(context, alertDialogProperties)
    }

    override fun makeToast(message: String) {
        BaseAlertDialog.makeToast(context, message)
    }
}