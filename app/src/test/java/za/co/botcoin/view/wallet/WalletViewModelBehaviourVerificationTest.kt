package za.co.botcoin.view.wallet

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
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
}