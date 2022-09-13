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
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.model.repository.stopOrder.StopOrderRepository
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.notificationService.INotificationService
import za.co.botcoin.view.wallet.WalletViewModel
import za.co.botcoin.view.wallet.WithdrawViewModel
import za.co.botcoin.view.wallet.menu.OrdersViewModel
import za.co.botcoin.view.wallet.menu.SendViewModel

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
    ): ViewModel =
        WalletViewModel(application, alertDialogService, resourceManager, balanceRepository)

    @Provides
    @IntoMap
    @ViewModelKey(OrdersViewModel::class)
    fun ordersViewModel(
        application: Application,
        orderRepository: OrderRepository,
        stopOrderRepository: StopOrderRepository
    ): ViewModel = OrdersViewModel(application, orderRepository, stopOrderRepository)

    @Provides
    @IntoMap
    @ViewModelKey(WithdrawViewModel::class)
    fun withdrawViewModel(
        application: Application,
        alertDialogService: IAlertDialogService,
        resourceManager: IResourceManager,
        notificationService: INotificationService,
        withdrawalRepository: WithdrawalRepository
    ): ViewModel = WithdrawViewModel(
        application,
        alertDialogService,
        resourceManager,
        notificationService,
        withdrawalRepository
    )

    @Provides
    @IntoMap
    @ViewModelKey(SendViewModel::class)
    fun sendViewModel(application: Application, sendRepository: SendRepository): ViewModel =
        SendViewModel(application, sendRepository)
}