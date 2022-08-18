package za.co.botcoin.di

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import za.co.botcoin.di.featureModules.AccountViewModelModule
import za.co.botcoin.di.featureModules.TickersViewModelModule
import za.co.botcoin.utils.ProgressDialog

@Subcomponent(modules = [ViewModelsModule::class, PresentationModule::class, TickersViewModelModule::class, AccountViewModelModule::class])
interface ActivityComponent {
    fun getViewModelFactory(): ViewModelProvider.Factory
    fun getProgressDialogService(): ProgressDialog
}