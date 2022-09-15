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
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuTrailingStopViewModel(application, alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuTrailingStartViewModel::class)
    fun menuTrailingStartViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuTrailingStartViewModel(application, alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuSupportPriceCounterViewModel::class)
    fun menuSupportPriceCounterViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuSupportPriceCounterViewModel(application, alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuResistancePriceCounterViewModel::class)
    fun menuResistancePriceCounterViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuResistancePriceCounterViewModel(application, alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuSmartTrendDetectorViewModel::class)
    fun menuSmartTrendDetectorViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuSmartTrendDetectorViewModel(application, alertDialogService, resourceManager, sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(MenuLunoApiViewModel::class)
    fun menuLunoApiViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        sharedPreferencesService: ISharedPreferencesService
    ) : ViewModel = MenuLunoApiViewModel(application, alertDialogService, resourceManager, sharedPreferencesService)

}