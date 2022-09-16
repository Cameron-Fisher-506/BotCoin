package za.co.botcoin.utils.services.clipBoardService

import android.content.Context

class ClipBoardService(private val context: Context): IClipBoardService {
    override fun copyToClipBoard(data: String) {
        BaseClipBoardService.copyToClipBoard(context, data)
    }

}