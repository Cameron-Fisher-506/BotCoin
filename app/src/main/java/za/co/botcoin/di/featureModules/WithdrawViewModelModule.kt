package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.view.wallet.WithdrawViewModel

@Module
class WithdrawViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(WithdrawViewModel::class)
    fun withdrawViewModel(application: Application, withdrawalRepository: WithdrawalRepository): ViewModel = WithdrawViewModel(application, withdrawalRepository)
}