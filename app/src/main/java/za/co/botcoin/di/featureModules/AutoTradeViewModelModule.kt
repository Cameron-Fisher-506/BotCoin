package za.co.botcoin.di.featureModules

import androidx.lifecycle.ViewModel
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.utils.services.alertDialogService.AlertDialogService
import za.co.botcoin.view.settings.AutoTradeViewModel

class AutoTradeViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(AutoTradeViewModel::class)
    fun autoTradeViewModel(alertDialogService: AlertDialogService): ViewModel = AutoTradeViewModel(alertDialogService)
}