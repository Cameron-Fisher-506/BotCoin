package za.co.botcoin.view.trade

import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Candle
import za.co.botcoin.model.models.Trade
import za.co.botcoin.utils.Resource

@ExperimentalCoroutinesApi
@DisplayName("Trade ViewModel State Verification Test")
class TradeViewModelStateVerificationTest : TradeViewModelTest() {

    @Test
    @DisplayName("Fetch Trades")
    fun shouldReturnTradesResponseWhenFetchTradesIsCalled() {
        val response: Resource<List<Trade>> = Resource.success(listOf(Trade(), Trade()))

        runBlockingTest {
            Mockito.`when`(tradeRepository.fetchTrades(anyString(), anyBoolean())).thenReturn(response)
        }

        tradeViewModel.fetchTrades("", false)
        with(tradeViewModel.tradeLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(tradeViewModel.tradeLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }

    @Test
    @DisplayName("Fetch Candles")
    fun shouldReturnCandlesResponseWhenFetchCandlesIsCalled() {
        val response: Resource<List<Candle>> = Resource.success(listOf(Candle(), Candle()))

        runBlockingTest {
            Mockito.`when`(candleRepository.fetchCandles(anyString(), anyString(), anyInt())).thenReturn(response)
        }

        candleViewModel.fetchCandles("", "", 0)
        with(candleViewModel.candleLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(candleViewModel.candleLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}