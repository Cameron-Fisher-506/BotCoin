package za.co.botcoin.view.home

import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import za.co.botcoin.getOrAwaitValue

@ExperimentalCoroutinesApi
class TickersViewModelBehaviourVerificationTest : TickersViewModelTest() {

    @Test
    fun shouldCallAccountRepositoryWhenFetchTickersIsCalled() {
        tickersViewModel.fetchTickers()
        tickersViewModel.tickersLiveData.getOrAwaitValue()
        runBlocking {
            verify(accountRepository).fetchTickers()
            verifyNoMoreInteractions(accountRepository)
        }
        assertTrue(true)
    }
}