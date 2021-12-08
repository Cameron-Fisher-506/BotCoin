package za.co.botcoin.view.home

import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.utils.Resource

@ExperimentalCoroutinesApi
class HomeViewModelStateVerificationTest : HomeViewModelTest() {

    @Test
    fun shouldReturnTickerResponseWhenFetchTickersIsCalled() {
        val tickers: Resource<List<Ticker>> = Resource.success(listOf(Ticker(), Ticker()))

        runBlocking {
            Mockito.`when`(tickersRepository.fetchTickers()).thenReturn(tickers)
        }

        tickersViewModel.fetchTickers()
        with(tickersViewModel.tickersLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}