package za.co.botcoin.utils.services.alertDialogService

import android.app.AlertDialog
import android.content.Context

object BaseAlertDialog {
    fun showAlertDialog(context: Context, alertDialogProperties: AlertDialogProperties) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(alertDialogProperties.title)
        builder.setCancelable(false)
        builder.setPositiveButton("Okay") { dialog, _ -> dialog.cancel() }
        builder.create().show()
    }
}