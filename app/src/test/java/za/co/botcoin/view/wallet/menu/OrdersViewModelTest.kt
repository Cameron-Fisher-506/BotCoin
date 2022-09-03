package za.co.botcoin.view.wallet.menu

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import za.co.botcoin.disposeObserver
import za.co.botcoin.model.repository.order.OrderRepository
import za.co.botcoin.model.repository.stopOrder.StopOrderRepository
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class OrdersViewModelTest: BaseViewModelTest() {
    @Mock
    private lateinit var ordersRepository: OrderRepository

    @Mock
    private lateinit var stopOrderRepository: StopOrderRepository

    @InjectMocks
    private lateinit var ordersViewModel: OrdersViewModel

    @BeforeEach
    fun setUp() {
        ordersViewModel.ioDispatcher = unconfinedTestDispatcher
    }

    @Test
    @DisplayName("Stop Order")
    fun shouldCallStopOrderRepositoryWhenStopOrderIsCalled() {
        ordersViewModel.stopOrder("")
        ordersViewModel.stopOrderResponse.disposeObserver()
        runBlocking {
            verify(stopOrderRepository).stopOrder(anyString())
            verifyNoMoreInteractions(stopOrderRepository)
        }
    }

    @Test
    @DisplayName("Fetch Orders")
    fun shouldCallOrderRepositoryWhenFetchOrdersIsCalled() {
        ordersViewModel.fetchOrders()
        ordersViewModel.ordersResponse.disposeObserver()
        runBlocking {
            verify(ordersRepository).fetchOrders()
            verifyNoMoreInteractions(ordersRepository)
        }
    }
}