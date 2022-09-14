package za.co.botcoin.view.wallet.menu

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.StopOrder
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.order.OrderRepository
import za.co.botcoin.model.repository.stopOrder.StopOrderRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.notificationService.INotificationService
import javax.inject.Inject

class WalletMenuOrdersViewModel @Inject constructor(
    application: Application,
    private val resourceManager: IResourceManager,
    private val notificationService: INotificationService,
    private val orderRepository: OrderRepository,
    private val stopOrderRepository: StopOrderRepository
) : BaseViewModel(application) {
    lateinit var ordersResponse: LiveData<Resource<List<Order>>>
    lateinit var stopOrderResponse: LiveData<Resource<List<StopOrder>>>

    fun fetchOrders() {
        ordersResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(orderRepository.fetchOrders())
        }
    }

    fun stopOrder(orderId: String) {
        stopOrderResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(stopOrderRepository.stopOrder(orderId))
        }
    }

    fun displayOrderCancellationSuccessNotification() {
        notificationService.notify(resourceManager.getString(R.string.wallet_order_cancellation), resourceManager.getString(R.string.wallet_order_cancelled_successfully))
    }

    fun displayOrderCancellationFailureNotification() {
        notificationService.notify(resourceManager.getString(R.string.wallet_order_cancellation), resourceManager.getString(R.string.wallet_order_cancellation_failed))
    }
}