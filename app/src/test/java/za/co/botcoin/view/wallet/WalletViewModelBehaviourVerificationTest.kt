package za.co.botcoin.view.wallet

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito.*
import za.co.botcoin.disposeObserver

@DisplayName("Wallet ViewModel Behaviour Verification Test")
class WalletViewModelBehaviourVerificationTest : WalletViewModelTest() {

    @Test
    @DisplayName("Fetch Balances")
    fun shouldCallBalanceRepositoryWhenFetchBalancesIsCalled() {
        balanceViewModel.fetchBalances()
        balanceViewModel.balancesLiveData.disposeObserver()
        runBlocking {
            verify(balanceRepository).fetchBalances()
            verifyNoMoreInteractions(balanceRepository)
        }
    }

    @Test
    @DisplayName("Withdrawal")
    fun shouldCallWithdrawalRepositoryWhenWithdrawalIsCalled() {
        withdrawalViewModel.withdrawal("", "", "")
        withdrawalViewModel.withdrawalLiveData.disposeObserver()
        runBlocking {
            verify(withdrawalRepository).withdrawal(anyString(), anyString(), anyString())
            verifyNoMoreInteractions(withdrawalRepository)
        }
    }

    @Test
    @DisplayName("Stop Order")
    fun shouldCallStopOrderRepositoryWhenStopOrderIsCalled() {
        stopOrderViewModel.stopOrder("")
        stopOrderViewModel.stopOrderLiveData.disposeObserver()
        runBlocking {
            verify(stopOrderRepository).stopOrder(anyString())
            verifyNoMoreInteractions(stopOrderRepository)
        }
    }

    @Test
    @DisplayName("Fetch Orders")
    fun shouldCallOrderRepositoryWhenFetchOrdersIsCalled() {
        orderViewModel.fetchOrders()
        orderViewModel.ordersLiveData.disposeObserver()
        runBlocking {
            verify(orderRepository).fetchOrders()
            verifyNoMoreInteractions(orderRepository)
        }
    }

    @Test
    @DisplayName("Receive")
    fun shouldCallReceiveRepositoryWhenReceiveIsCalled() {
        receiveViewModel.receive("", "", "")
        receiveViewModel.receiveLiveData.disposeObserver()
        runBlocking {
            verify(receiveRepository).receive(anyString(), anyString(), anyString())
            verifyNoMoreInteractions(receiveRepository)
        }
    }

    @Test
    @DisplayName("Send")
    fun shouldCallSendRepositoryWhenSendIsCalled() {
        sendViewModel.send("", "", "", "")
        sendViewModel.sendLiveData.disposeObserver()
        runBlocking {
            verify(sendRepository).send(anyString(), anyString(), anyString(), anyString())
            verifyNoMoreInteractions(sendRepository)
        }
    }

    @Test
    @DisplayName("Post Order")
    fun shouldCallPostOrderRepositoryWhenPostOrderIsCalled() {
        postOrderViewModel.postOrder("", "", "", "")
        postOrderViewModel.postOrderLiveData.disposeObserver()
        runBlocking {
            verify(postOrderRepository).postOrder(anyString(), anyString(), anyString(), anyString())
            verifyNoMoreInteractions(postOrderRepository)
        }
    }
}