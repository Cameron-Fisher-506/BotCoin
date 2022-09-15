package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharePreferencesService.ISharedPreferencesService
import za.co.botcoin.view.menu.*

@Module
class MenuViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(MenuTrailingStopViewModel::class)
    fun menuTrailingStopViewModel(
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuTrailingStopViewModel(alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuTrailingStartViewModel::class)
    fun menuTrailingStartViewModel(
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuTrailingStartViewModel(alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuSupportPriceCounterViewModel::class)
    fun menuSupportPriceCounterViewModel(
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuSupportPriceCounterViewModel(alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuResistancePriceCounterViewModel::class)
    fun menuResistancePriceCounterViewModel(
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuResistancePriceCounterViewModel(alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuSmartTrendDetectorViewModel::class)
    fun menuSmartTrendDetectorViewModel(
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuSmartTrendDetectorViewModel(alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuLunoApiViewModel::class)
    fun menuLunoApiViewModel(
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuLunoApiViewModel(alertDialogService, resourceManager, sharedPreferencesService)

}