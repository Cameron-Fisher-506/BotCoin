package za.co.botcoin.view.home

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import za.co.botcoin.disposeObserver

class HomeViewModelBehaviourVerificationTest : HomeViewModelTest() {

    @Test
    fun shouldCallTickersRepositoryWhenFetchTickersIsCalled() {
        tickersViewModel.fetchTickers()
        tickersViewModel.tickersLiveData.disposeObserver()
        runBlocking {
            verify(tickersRepository).fetchTickers()
            verifyNoMoreInteractions(tickersRepository)
        }
    }
}