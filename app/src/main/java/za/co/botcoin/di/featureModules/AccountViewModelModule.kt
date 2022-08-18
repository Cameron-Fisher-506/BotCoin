package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.model.repository.account.AccountViewModel

@Module
class AccountViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun accountViewModel(application: Application): ViewModel = AccountViewModel(application)
}