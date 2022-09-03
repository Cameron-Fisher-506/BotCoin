package za.co.botcoin.di

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import za.co.botcoin.di.featureModules.HomeViewModelModule
import za.co.botcoin.di.featureModules.WalletViewModelModule
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.AlertDialogService
import za.co.botcoin.utils.services.ProgressDialogService

@Subcomponent(modules = [ViewModelsModule::class, PresentationModule::class, HomeViewModelModule::class, WalletViewModelModule::class])
interface ActivityComponent {
    fun getViewModelFactory(): ViewModelProvider.Factory
    fun getProgressDialogService(): ProgressDialogService
    fun getAlertDialogService(): AlertDialogService
    fun getResourceManager(): IResourceManager
}