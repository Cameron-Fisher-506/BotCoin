package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.model.repository.tickers.TickersViewModel

@Module
class TickersViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(TickersViewModel::class)
    fun tickersViewModel(tickersRepository: TickersRepository): ViewModel = TickersViewModel(tickersRepository)
}