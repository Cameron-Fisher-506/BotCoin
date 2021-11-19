package za.co.botcoin.view.home

import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.*
import za.co.botcoin.disposeObserver
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.utils.Resource

@ExperimentalCoroutinesApi
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