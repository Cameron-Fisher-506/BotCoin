package za.co.ticker.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object ClipBoardUtils {
    fun copyToClipBoard(context: Context, data: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("data", data)
        clipboard.setPrimaryClip(clip)
        GeneralUtils.makeToast(context, "Copied to clipboard!")
    }
}