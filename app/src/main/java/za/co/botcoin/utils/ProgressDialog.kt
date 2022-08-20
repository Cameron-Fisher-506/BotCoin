package za.co.botcoin.utils

import android.app.Dialog
import android.content.Context
import za.co.botcoin.R

class ProgressDialog(context: Context) : Dialog(context) {

    init {
        this.setContentView(R.layout.progress_bar)
        this.show()
    }
}