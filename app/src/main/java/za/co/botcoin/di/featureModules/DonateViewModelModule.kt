package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.clipBoardService.IClipBoardService
import za.co.botcoin.utils.services.notificationService.INotificationService
import za.co.botcoin.view.menu.donate.DonateMenuDonateViewModel

@Module
class DonateViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(DonateMenuDonateViewModel::class)
    fun donateViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        notificationService: INotificationService,
        clipBoardService: IClipBoardService,
        receiveRepository: ReceiveRepository,
        sendRepository: SendRepository
    ): ViewModel = DonateMenuDonateViewModel(application, alertDialogService, resourceManager, notificationService, clipBoardService, receiveRepository, sendRepository)
}