package za.co.botcoin.di.featureModules

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.view.settings.AutoTradeViewModel

@Module
class AutoTradeViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(AutoTradeViewModel::class)
    fun autoTradeViewModel(
        resourceManager: IResourceManager,
        alertDialogService: IAlertDialogService
    ): ViewModel = AutoTradeViewModel(resourceManager, alertDialogService)
}