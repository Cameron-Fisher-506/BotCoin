package za.co.botcoin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.botcoin.utils.ProgressDialog

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar = ProgressDialog(this)
    }

    fun displayProgressBar() {
        progressBar.show()
    }

    fun dismissProgressBar() {
        progressBar.dismiss()
    }
}