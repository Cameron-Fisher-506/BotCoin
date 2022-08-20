package za.co.botcoin.view.home

import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.utils.Resource

@ExperimentalCoroutinesApi
@DisplayName("Home ViewModel State Verification Test")
class HomeViewModelStateVerificationTest : HomeViewModelTest() {

    @Test
    @DisplayName("Fetch Tickers")
    fun shouldReturnTickerResponseWhenFetchTickersIsCalled() {
        val tickers: Resource<List<Ticker>> = Resource.success(listOf(Ticker(), Ticker()))

        runBlockingTest {
            Mockito.`when`(tickersRepository.fetchTickers()).thenReturn(tickers)
        }

        homeViewModel.fetchTickers()
        with(homeViewModel.tickersResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}