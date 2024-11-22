package za.co.botcoin.di.featureModules

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService
import za.co.botcoin.view.settings.AutoTradeSwitchViewModel
import za.co.botcoin.view.settings.AutoTradeViewModel

@Module
class AutoTradeModule {
    @Provides
    @IntoMap
    @ViewModelKey(AutoTradeViewModel::class)
    fun autoTradeViewModel(
        resourceManager: IResourceManager,
        alertDialogService: IAlertDialogService,
        sharedPreferencesService: ISharedPreferencesService
    ): ViewModel = AutoTradeViewModel(resourceManager, alertDialogService, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(AutoTradeSwitchViewModel::class)
    fun autoTradeSwitchViewModel(
        resourceManager: IResourceManager,
        alertDialogService: IAlertDialogService,
        sharedPreferencesService: ISharedPreferencesService
    ): ViewModel = AutoTradeSwitchViewModel(resourceManager, alertDialogService, sharedPreferencesService)
}