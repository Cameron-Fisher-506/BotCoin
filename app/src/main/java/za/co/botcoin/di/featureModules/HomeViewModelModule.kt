package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.model.repository.account.AccountRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.view.home.HomeViewModel

@Module
class HomeViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModel(application: Application, accountRepository: AccountRepository, tickersRepository: TickersRepository): ViewModel = HomeViewModel(application, accountRepository, tickersRepository)

}