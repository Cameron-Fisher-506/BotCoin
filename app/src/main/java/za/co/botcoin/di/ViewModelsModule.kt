package za.co.botcoin.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.model.repository.ViewModelFactory
import za.co.botcoin.model.repository.tickers.TickersViewModel
import javax.inject.Provider

@Module
class ViewModelsModule {
    @Provides
    fun getViewModelFactory(providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory = ViewModelFactory(providers)
}