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
import za.co.botcoin.disposeObserver
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.StopOrder
import za.co.botcoin.model.repository.order.OrderRepository
import za.co.botcoin.model.repository.stopOrder.StopOrderRepository
import za.co.botcoin.utils.Resource
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
    @DisplayName("Stop Order Behavioural Verification")
    fun shouldCallStopOrderRepositoryWhenStopOrderIsCalled() {
        ordersViewModel.stopOrder("")
        ordersViewModel.stopOrderResponse.disposeObserver()
        runBlocking {
            verify(stopOrderRepository).stopOrder(anyString())
            verifyNoMoreInteractions(stopOrderRepository)
        }
    }

    @Test
    @DisplayName("Fetch Orders Behavioural Verification")
    fun shouldCallOrderRepositoryWhenFetchOrdersIsCalled() {
        ordersViewModel.fetchOrders()
        ordersViewModel.ordersResponse.disposeObserver()
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

        ordersViewModel.stopOrder("")
        with(ordersViewModel.stopOrderResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(ordersViewModel.stopOrderResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
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

        ordersViewModel.fetchOrders()
        with(ordersViewModel.ordersResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(ordersViewModel.ordersResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}