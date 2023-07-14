package za.co.botcoin.di

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import za.co.botcoin.di.featureModules.*
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.ProgressDialogService
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.clipBoardService.IClipBoardService
import za.co.botcoin.utils.services.notificationService.INotificationService
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService

@ActivityScope
@Subcomponent(modules = [ViewModelsModule::class, PresentationModule::class, HomeModule::class, WalletModule::class, DonateModule::class, AutoTradeModule::class, MenuModule::class])
interface ActivityComponent {
    fun getViewModelFactory(): ViewModelProvider.Factory
    fun getProgressDialogService(): ProgressDialogService
    fun getAlertDialogService(): IAlertDialogService
    fun getResourceManager(): IResourceManager
    fun getSharedPreferencesService(): ISharedPreferencesService
    fun getNotificationService(): INotificationService
    fun getClipBoardService(): IClipBoardService
}