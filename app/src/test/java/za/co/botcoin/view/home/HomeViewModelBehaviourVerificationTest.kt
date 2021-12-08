package za.co.botcoin.view.home

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import za.co.botcoin.disposeObserver

@DisplayName("Home ViewModel Behaviour Verification Test")
class HomeViewModelBehaviourVerificationTest : HomeViewModelTest() {

    @Test
    @DisplayName("Fetch Tickers")
    fun shouldCallTickersRepositoryWhenFetchTickersIsCalled() {
        tickersViewModel.fetchTickers()
        tickersViewModel.tickersLiveData.disposeObserver()
        runBlocking {
            verify(tickersRepository).fetchTickers()
            verifyNoMoreInteractions(tickersRepository)
        }
    }
}