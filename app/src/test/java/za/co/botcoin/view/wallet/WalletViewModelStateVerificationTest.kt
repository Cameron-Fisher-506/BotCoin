package za.co.botcoin.view.wallet

import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.models.StopOrder
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.utils.Resource

class WalletViewModelStateVerificationTest : WalletViewModelTest() {

    @Test
    fun shouldReturnBalanceResponseWhenFetchBalancesIsCalled() {
        val balances: Resource<List<Balance>> = Resource(Status.SUCCESS, listOf(Balance(), Balance()), "Success")

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
    fun shouldReturnWithdrawalResponseWhenWithdrawIsCalled() {
        val withdrawal: Resource<List<Withdrawal>> = Resource(Status.SUCCESS, listOf(Withdrawal()), "")

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
    fun shouldReturnStopOrderResponseWhenStopOrderIsCalled() {
        val stopOrder: Resource<List<StopOrder>> = Resource(Status.SUCCESS, listOf(StopOrder()), "")

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
    fun shouldReturnOrdersResponseWhenFetchOrdersIsCalled() {
        val orders: Resource<List<Order>> = Resource(Status.SUCCESS, listOf(Order(), Order()), "")

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
}