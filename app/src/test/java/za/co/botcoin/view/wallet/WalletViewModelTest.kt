package za.co.botcoin.view.wallet

import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import za.co.botcoin.disposeObserver
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.*
import za.co.botcoin.model.repository.balance.BalanceRepository
import za.co.botcoin.model.repository.order.OrderRepository
import za.co.botcoin.model.repository.postOrder.PostOrderRepository
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.model.repository.stopOrder.StopOrderRepository
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class WalletViewModelTest : BaseViewModelTest() {

    @Mock
    private lateinit var balanceRepository: BalanceRepository

    @Mock
    private lateinit var withdrawalRepository: WithdrawalRepository

    @Mock
    private lateinit var stopOrderRepository: StopOrderRepository

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var receiveRepository: ReceiveRepository

    @Mock
    private lateinit var sendRepository: SendRepository

    @Mock
    private lateinit var postOrderRepository: PostOrderRepository

    @InjectMocks
    private lateinit var walletViewModel: WalletViewModel

    @Before
    fun setUp() {
        walletViewModel.ioDispatcher = unconfinedTestDispatcher
    }

    @Test
    @DisplayName("Fetch Balances")
    fun shouldCallBalanceRepositoryWhenFetchBalancesIsCalled() {
        walletViewModel.fetchBalances()
        walletViewModel.balancesResponse.disposeObserver()
        runBlocking {
            Mockito.verify(balanceRepository).fetchBalances()
            Mockito.verifyNoMoreInteractions(balanceRepository)
        }
    }

    /*





    @Test
    @DisplayName("Post Order")
    fun shouldCallPostOrderRepositoryWhenPostOrderIsCalled() {
        postOrderViewModel.postOrder("", "", "", "")
        postOrderViewModel.postOrderLiveData.disposeObserver()
        runBlocking {
            Mockito.verify(postOrderRepository).postOrder(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
            Mockito.verifyNoMoreInteractions(postOrderRepository)
        }
    }

    @Test
    @DisplayName("Fetch Balances")
    fun shouldReturnBalanceResponseWhenFetchBalancesIsCalled() {
        val balances: Resource<List<Balance>> = Resource.success(listOf(Balance()))

        runTest {
            Mockito.`when`(balanceRepository.fetchBalances()).thenReturn(balances)
        }

        walletViewModel.fetchBalances()
        with(walletViewModel.balancesResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this.status)
        }

        with(walletViewModel.balancesResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this.status)
            assertTrue(!this.data.isNullOrEmpty())
        }
    }

    @Test
    @DisplayName("Withdrawal")
    fun shouldReturnWithdrawalResponseWhenWithdrawalIsCalled() {
        val withdrawal: Resource<List<Withdrawal>> = Resource.success(listOf(Withdrawal()))

        runBlockingTest {
            Mockito.`when`(withdrawalRepository.withdrawal(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(withdrawal)
        }

        withdrawalViewModel.withdrawal("", "", "")
        with(withdrawalViewModel.withdrawalLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(withdrawalViewModel.withdrawalLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }

    @Test
    @DisplayName("Stop Order")
    fun shouldReturnStopOrderResponseWhenStopOrderIsCalled() {
        val stopOrder: Resource<List<StopOrder>> = Resource.success(listOf(StopOrder()))

        runBlockingTest {
            Mockito.`when`(stopOrderRepository.stopOrder(ArgumentMatchers.anyString())).thenReturn(stopOrder)
        }

        stopOrderViewModel.stopOrder("")
        with(stopOrderViewModel.stopOrderLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(stopOrderViewModel.stopOrderLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }

    @Test
    @DisplayName("Fetch Orders")
    fun shouldReturnOrdersResponseWhenFetchOrdersIsCalled() {
        val orders: Resource<List<Order>> = Resource.success(listOf(Order(), Order()))

        runBlockingTest {
            Mockito.`when`(orderRepository.fetchOrders()).thenReturn(orders)
        }

        orderViewModel.fetchOrders()
        with(orderViewModel.ordersLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(orderViewModel.ordersLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }

    @Test
    @DisplayName("Receive")
    fun shouldReturnReceiveResponseWhenReceiveIsCalled() {
        val receive: Resource<List<Receive>> = Resource.success(listOf(Receive()))

        runBlockingTest {
            Mockito.`when`(receiveRepository.receive(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(receive)
        }

        receiveViewModel.receive("", "", "")
        with(receiveViewModel.receiveLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(receiveViewModel.receiveLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }

    @Test
    @DisplayName("send")
    fun shouldReturnSendResponseWhenSendIsCalled() {
        val send: Resource<List<Send>> = Resource.success(listOf(Send()))

        runBlockingTest {
            Mockito.`when`(sendRepository.send(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(send)
        }

        sendViewModel.send("", "", "", "")
        with(sendViewModel.sendLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(sendViewModel.sendLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }

    @Test
    @DisplayName("Post Order")
    fun shouldReturnPostOrderResponseWhenPostOrderIsCalled() {
        val postOrder: Resource<List<PostOrder>> = Resource.success(listOf(PostOrder()))

        runBlockingTest {
            Mockito.`when`(postOrderRepository.postOrder(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(postOrder)
        }

        postOrderViewModel.postOrder("", "", "", "")
        with(postOrderViewModel.postOrderLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(postOrderViewModel.postOrderLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }*/
}