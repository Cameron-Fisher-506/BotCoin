package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.model.repository.balance.BalanceRepository
import za.co.botcoin.view.wallet.WalletViewModel

@Module
class WalletViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(WalletViewModel::class)
    fun walletViewModel(application: Application, balanceRepository: BalanceRepository): ViewModel = WalletViewModel(application, balanceRepository)
}