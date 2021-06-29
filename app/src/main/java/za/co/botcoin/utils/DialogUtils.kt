package za.co.botcoin.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout

object DialogUtils {
    fun createAlertPermission(context: Context?, title: String?, message: String?, isPrompt: Boolean, permissionCallback: PermissionCallback): AlertDialog? {
        var toReturn: AlertDialog? = null
        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)
        builder.setMessage(message)

        if (isPrompt) {
            builder.setCancelable(true)
            builder.setPositiveButton("Yes") { dialog, id ->
                permissionCallback.checkPermission(true)
                dialog.cancel()
            }
            builder.setNegativeButton("No") { dialog, id -> dialog.cancel() }
        } else {
            builder.setCancelable(false)
            builder.setPositiveButton("Okay") { dialog, id -> dialog.cancel() }
        }

        toReturn = builder.create()
        return toReturn
    }

    fun createAlertDialog(context: Context?, title: String?, message: String?, isPrompt: Boolean): AlertDialog? {
        var toReturn: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val edTxtName = EditText(context)

        builder.setTitle(title)
        builder.setMessage(message)

        if (isPrompt) {
            val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.setMargins(40, 40, 40, 40)
            edTxtName.layoutParams = lp
            edTxtName.hint = "Name"
            builder.setView(edTxtName)
        }
        if (isPrompt) {
            builder.setCancelable(true)
            builder.setPositiveButton("Yes") { dialog, _ -> dialog.cancel() }
            builder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        } else {
            builder.setCancelable(false)
            builder.setPositiveButton("Okay") { dialog, _ -> dialog.cancel() }
        }

        toReturn = builder.create()
        return toReturn
    }
}