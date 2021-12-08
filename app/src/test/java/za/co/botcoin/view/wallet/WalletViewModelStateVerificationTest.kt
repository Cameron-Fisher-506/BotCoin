package za.co.botcoin.view.wallet

import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.*
import za.co.botcoin.utils.Resource

@ExperimentalCoroutinesApi
@DisplayName("Wallet ViewModel State Verification Test")
class WalletViewModelStateVerificationTest : WalletViewModelTest() {

    @Test
    @DisplayName("Fetch Balances")
    fun shouldReturnBalanceResponseWhenFetchBalancesIsCalled() {
        val balances: Resource<List<Balance>> = Resource.success(listOf(Balance()))

        runBlocking {
            Mockito.`when`(balanceRepository.fetchBalances()).thenReturn(balances)
        }

        balanceViewModel.fetchBalances()
        with(balanceViewModel.balancesLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }

    @Test
    @DisplayName("Withdrawal")
    fun shouldReturnWithdrawalResponseWhenWithdrawalIsCalled() {
        val withdrawal: Resource<List<Withdrawal>> = Resource.success(listOf(Withdrawal()))

        runBlocking {
            Mockito.`when`(withdrawalRepository.withdrawal(anyString(), anyString(), anyString())).thenReturn(withdrawal)
        }

        withdrawalViewModel.withdrawal("", "", "")
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

        runBlocking {
            Mockito.`when`(stopOrderRepository.stopOrder(anyString())).thenReturn(stopOrder)
        }

        stopOrderViewModel.stopOrder("")
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

        runBlocking {
            Mockito.`when`(orderRepository.fetchOrders()).thenReturn(orders)
        }

        orderViewModel.fetchOrders()
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

        runBlocking {
            Mockito.`when`(receiveRepository.receive(anyString(), anyString(), anyString())).thenReturn(receive)
        }

        receiveViewModel.receive("", "", "")
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

        runBlocking {
            Mockito.`when`(sendRepository.send(anyString(), anyString(), anyString(), anyString())).thenReturn(send)
        }

        sendViewModel.send("", "", "", "")
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

        runBlocking {
            Mockito.`when`(postOrderRepository.postOrder(anyString(), anyString(), anyString(), anyString())).thenReturn(postOrder)
        }

        postOrderViewModel.postOrder("", "", "", "")
        with(postOrderViewModel.postOrderLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}