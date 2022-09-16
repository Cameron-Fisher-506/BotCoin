package za.co.botcoin.utils.services

import android.app.Dialog
import android.content.Context
import za.co.botcoin.R

class ProgressDialogService(context: Context) : Dialog(context) {

    init {
        this.setContentView(R.layout.progress_bar)
    }
}