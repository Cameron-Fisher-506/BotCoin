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
import za.co.botcoin.view.menu.MenuTrailingStopViewModel

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
}