package za.co.botcoin.view.wallet

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.*
import za.co.botcoin.disposeObserver

class WalletViewModelBehaviourVerificationTest : WalletViewModelTest() {

    @Test
    fun shouldCallBalanceRepositoryWhenFetchBalancesIsCalled() {
        balanceViewModel.fetchBalances()
        balanceViewModel.balancesLiveData.disposeObserver()
        runBlocking {
            verify(balanceRepository).fetchBalances()
            verifyNoMoreInteractions(balanceRepository)
        }
    }

    @Test
    fun shouldCallWithdrawalRepositoryWhenWithdrawalIsCalled() {
        withdrawalViewModel.withdrawal("", "", "")
        withdrawalViewModel.withdrawalLiveData.disposeObserver()
        runBlocking {
            verify(withdrawalRepository).withdrawal(anyString(), anyString(), anyString())
            verifyNoMoreInteractions(withdrawalRepository)
        }
    }

    @Test
    fun shouldCallStopOrderRepositoryWhenStopOrderIsCalled() {
        stopOrderViewModel.stopOrder("")
        stopOrderViewModel.stopOrderLiveData.disposeObserver()
        runBlocking {
            verify(stopOrderRepository).stopOrder(anyString())
            verifyNoMoreInteractions(stopOrderRepository)
        }
    }
}