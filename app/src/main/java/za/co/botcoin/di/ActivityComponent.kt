package za.co.botcoin.di

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import za.co.botcoin.di.featureModules.HomeViewModelModule
import za.co.botcoin.di.featureModules.WalletViewModelModule
import za.co.botcoin.di.featureModules.WithdrawViewModelModule
import za.co.botcoin.utils.ProgressDialog

@Subcomponent(modules = [ViewModelsModule::class, PresentationModule::class, HomeViewModelModule::class, WalletViewModelModule::class, WithdrawViewModelModule::class])
interface ActivityComponent {
    fun getViewModelFactory(): ViewModelProvider.Factory
    fun getProgressDialogService(): ProgressDialog
}