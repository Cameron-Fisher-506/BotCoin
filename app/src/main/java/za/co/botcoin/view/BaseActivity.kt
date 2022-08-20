package za.co.botcoin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.botcoin.BotCoinApplication
import za.co.botcoin.di.PresentationModule
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

    private val activityComponent by lazy {
        (application as BotCoinApplication).applicationComponent.getActivityComponent(PresentationModule(this))
    }

    val getViewModelFactory
        get() = activityComponent.getViewModelFactory()
}