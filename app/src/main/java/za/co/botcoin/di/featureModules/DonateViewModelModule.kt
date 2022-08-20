package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.view.menu.donate.DonateViewModel

@Module
class DonateViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(DonateViewModel::class)
    fun donateViewModel(application: Application, receiveRepository: ReceiveRepository, sendRepository: SendRepository): ViewModel = DonateViewModel(application, receiveRepository, sendRepository)
}