package za.co.botcoin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import za.co.botcoin.model.repository.ViewModelFactory
import javax.inject.Provider

@Module
class ViewModelsModule {
    @Provides
    fun getViewModelFactory(providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory = ViewModelFactory(providers)
}