package za.co.botcoin.di.featureModules

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import za.co.botcoin.di.ViewModelKey
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.repository.balance.BalanceRepository
import za.co.botcoin.model.repository.order.OrderRepository
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.model.repository.stopOrder.StopOrderRepository
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.notificationService.INotificationService
import za.co.botcoin.view.wallet.WalletViewModel
import za.co.botcoin.view.wallet.WalletWithdrawViewModel
import za.co.botcoin.view.wallet.menu.WalletMenuOrdersViewModel
import za.co.botcoin.view.wallet.menu.WalletMenuReceiveViewModel
import za.co.botcoin.view.wallet.menu.WalletMenuSendViewModel

@Module
class WalletViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(WalletViewModel::class)
    fun walletViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        balanceRepository: BalanceRepository
    ): ViewModel = WalletViewModel(application, alertDialogService, resourceManager, balanceRepository)

    @Provides
    @IntoMap
    @ViewModelKey(WalletMenuOrdersViewModel::class)
    fun walletMenuOrdersViewModel(
        application: Application,
        resourceManager: IResourceManager,
        notificationService: INotificationService,
        orderRepository: OrderRepository,
        stopOrderRepository: StopOrderRepository
    ): ViewModel = WalletMenuOrdersViewModel(application, resourceManager, notificationService, orderRepository, stopOrderRepository)

    @Provides
    @IntoMap
    @ViewModelKey(WalletWithdrawViewModel::class)
    fun walletWithdrawViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        notificationService: INotificationService,
        withdrawalRepository: WithdrawalRepository
    ): ViewModel = WalletWithdrawViewModel(application, alertDialogService, resourceManager, notificationService, withdrawalRepository)

    @Provides
    @IntoMap
    @ViewModelKey(WalletMenuSendViewModel::class)
    fun walletMenuSendViewModel(
        application: Application,
        resourceManager: IResourceManager,
        notificationService: INotificationService,
        sendRepository: SendRepository
    ): ViewModel = WalletMenuSendViewModel(application, resourceManager, notificationService, sendRepository)

    @Provides
    @IntoMap
    @ViewModelKey(WalletMenuReceiveViewModel::class)
    fun walletMenuReceiveViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        receiveRepository: ReceiveRepository
    ): ViewModel = WalletMenuReceiveViewModel(application, alertDialogService, resourceManager, receiveRepository)
}