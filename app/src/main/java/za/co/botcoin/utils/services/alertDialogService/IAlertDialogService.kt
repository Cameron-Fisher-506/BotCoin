package za.co.botcoin.utils.services.alertDialogService

import android.content.Context

interface IAlertDialogService {
    fun showAlertDialog(alertDialogProperties: AlertDialogProperties)
    fun makeToast(message: String)
}