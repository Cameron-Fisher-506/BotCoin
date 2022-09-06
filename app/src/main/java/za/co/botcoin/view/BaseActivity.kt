package za.co.botcoin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.co.botcoin.BotCoinApplication
import za.co.botcoin.di.PresentationModule
import za.co.botcoin.utils.services.ProgressDialogService

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var progressDialogBar: ProgressDialogService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialogBar = ProgressDialogService(this)
    }

    fun displayProgressBar() {
        progressDialogBar.show()
    }

    fun dismissProgressBar() {
        progressDialogBar.dismiss()
    }

    private val activityComponent by lazy {
        (application as BotCoinApplication).applicationComponent.getActivityComponent(PresentationModule(this))
    }

    val getViewModelFactory
        get() = activityComponent.getViewModelFactory()
}