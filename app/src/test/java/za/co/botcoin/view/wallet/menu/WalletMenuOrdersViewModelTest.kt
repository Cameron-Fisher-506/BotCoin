package za.co.botcoin.view.wallet.menu

import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.disposeObserver
import za.co.botcoin.state.ServiceState
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.StopOrder
import za.co.botcoin.model.repository.order.OrderRepository
import za.co.botcoin.model.repository.stopOrder.StopOrderRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.notificationService.INotificationService
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class WalletMenuOrdersViewModelTest: BaseViewModelTest() {
    @Mock
    private lateinit var resourceManager: IResourceManager

    @Mock
    private lateinit var notificationService: INotificationService

    @Mock
    private lateinit var ordersRepository: OrderRepository

    @Mock
    private lateinit var stopOrderRepository: StopOrderRepository

    @InjectMocks
    private lateinit var walletMenuOrdersViewModel: WalletMenuOrdersViewModel

    @BeforeEach
    fun setUp() {
        walletMenuOrdersViewModel.ioDispatcher = unconfinedTestDispatcher
    }

    @Test
    @DisplayName("Stop Order Behavioural Verification")
    fun shouldCallStopOrderRepositoryWhenStopOrderIsCalled() {
        walletMenuOrdersViewModel.stopOrder("")
        walletMenuOrdersViewModel.stopOrderResponse.disposeObserver()
        runBlocking {
            verify(stopOrderRepository).stopOrder(anyString())
            verifyNoMoreInteractions(stopOrderRepository)
        }
    }

    @Test
    @DisplayName("Fetch Orders Behavioural Verification")
    fun shouldCallOrderRepositoryWhenFetchOrdersIsCalled() {
        walletMenuOrdersViewModel.fetchOrders()
        walletMenuOrdersViewModel.ordersResponse.disposeObserver()
        runBlocking {
            verify(ordersRepository).fetchOrders()
            verifyNoMoreInteractions(ordersRepository)
        }
    }

    @Test
    @DisplayName("Stop Order State Verification")
    fun shouldReturnStopOrderResponseWhenStopOrderIsCalled() {
        val stopOrder: Resource<List<StopOrder>> = Resource.success(listOf(StopOrder()))

        runTest {
            `when`(stopOrderRepository.stopOrder(anyString())).thenReturn(stopOrder)
        }

        walletMenuOrdersViewModel.stopOrder("")
        with(walletMenuOrdersViewModel.stopOrderResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(ServiceState.Loading, this?.serviceState)
        }

        with(walletMenuOrdersViewModel.stopOrderResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(ServiceState.Success, this?.serviceState)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }

    @Test
    @DisplayName("Fetch Orders State Verification")
    fun shouldReturnOrdersResponseWhenFetchOrdersIsCalled() {
        val orders: Resource<List<Order>> = Resource.success(listOf(Order(), Order()))

        runTest {
            `when`(ordersRepository.fetchOrders()).thenReturn(orders)
        }

        walletMenuOrdersViewModel.fetchOrders()
        with(walletMenuOrdersViewModel.ordersResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(ServiceState.Loading, this?.serviceState)
        }

        with(walletMenuOrdersViewModel.ordersResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(ServiceState.Success, this?.serviceState)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}