package za.co.botcoin.view.home

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import za.co.botcoin.disposeObserver

class TickersViewModelBehaviourVerificationTest : TickersViewModelTest() {

    @Test
    fun shouldCallAccountRepositoryWhenFetchTickersIsCalled() {
        tickersViewModel.fetchTickers()
        tickersViewModel.tickersLiveData.disposeObserver()
        runBlocking {
            verify(accountRepository).fetchTickers()
            verifyNoMoreInteractions(accountRepository)
        }
    }
}