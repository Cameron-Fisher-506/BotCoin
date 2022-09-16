package za.co.botcoin.utils.services.clipBoardService

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import za.co.botcoin.utils.GeneralUtils

object BaseClipBoardService {
    fun copyToClipBoard(context: Context, data: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("data", data)
        clipboard.setPrimaryClip(clip)
    }
}