package za.co.botcoin.utils.services.alertDialogService

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

object BaseAlertDialog {
    fun showAlertDialog(context: Context, alertDialogProperties: AlertDialogProperties) = AlertDialog.Builder(context).apply {
        setTitle(alertDialogProperties.title)
        setMessage(alertDialogProperties.message)
        setCancelable(false)
        setPositiveButton("Okay") { dialog, _ -> dialog.cancel() }
        create().show()
    }

    fun makeToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}