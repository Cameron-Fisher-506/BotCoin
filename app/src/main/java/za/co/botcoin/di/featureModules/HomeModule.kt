package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.model.repository.account.AccountRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.utils.services.cacheService.ICacheService
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService
import za.co.botcoin.view.home.HomeDisclaimerPolicyViewModel
import za.co.botcoin.view.home.HomePrivacyPolicyViewModel
import za.co.botcoin.view.home.HomeTickerViewModel
import za.co.botcoin.view.home.HomeViewModel

@Module
class HomeModule {
    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModel(cacheService: ICacheService): ViewModel = HomeViewModel(cacheService)

    @Provides
    @IntoMap
    @ViewModelKey(HomeTickerViewModel::class)
    fun homeTickerViewModel(
        application: Application,
        sharedPreferencesService: ISharedPreferencesService,
        accountRepository: AccountRepository,
        tickersRepository: TickersRepository
    ): ViewModel = HomeTickerViewModel(application, sharedPreferencesService, accountRepository, tickersRepository)

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