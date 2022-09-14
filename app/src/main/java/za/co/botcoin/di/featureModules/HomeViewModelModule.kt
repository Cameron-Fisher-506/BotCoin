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
import za.co.botcoin.view.home.HomeDisclaimerPolicyViewModel
import za.co.botcoin.view.home.HomeViewModel
import za.co.botcoin.view.home.HomePrivacyPolicyViewModel

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
    @ViewModelKey(HomePrivacyPolicyViewModel::class)
    fun privacyViewModel(
        sharedPreferencesService: ISharedPreferencesService
    ): ViewModel = HomePrivacyPolicyViewModel(sharedPreferencesService)

    @Provides
    @IntoMap
    @ViewModelKey(HomeDisclaimerPolicyViewModel::class)
    fun disclaimerPolicyViewModel(
        sharedPreferencesService: ISharedPreferencesService
    ): ViewModel = HomeDisclaimerPolicyViewModel(sharedPreferencesService)
}