package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.model.repository.account.AccountRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.utils.services.sharePreferencesService.ISharedPreferencesService
import za.co.botcoin.utils.services.sharePreferencesService.SharedPreferencesService
import za.co.botcoin.view.home.DisclaimerPolicyViewModel
import za.co.botcoin.view.home.HomeViewModel
import za.co.botcoin.view.home.PrivacyPolicyViewModel

@Module
class HomeViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModel(
        application: Application,
        sharedPreferencesService: ISharedPreferencesService,
        accountRepository: AccountRepository,
        tickersRepository: TickersRepository
    ): ViewModel = HomeViewModel(application, sharedPreferencesService, accountRepository, tickersRepository)

    @Provides
    @IntoMap
    @ViewModelKey(PrivacyPolicyViewModel::class)
    fun privacyViewModel(
        sharedPreferencesService: ISharedPreferencesService
    ): ViewModel = PrivacyPolicyViewModel(sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(DisclaimerPolicyViewModel::class)
    fun disclaimerPolicyViewModel(
        sharedPreferencesService: ISharedPreferencesService
    ): ViewModel = DisclaimerPolicyViewModel(sharedPreferencesService)
}