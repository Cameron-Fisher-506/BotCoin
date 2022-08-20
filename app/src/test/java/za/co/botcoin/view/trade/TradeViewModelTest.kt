package za.co.botcoin.view.trade

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import za.co.botcoin.model.models.Candle
import za.co.botcoin.model.models.Trade
import za.co.botcoin.model.repository.candle.CandleRepository
import za.co.botcoin.model.repository.trade.TradeRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.view.BaseViewModelTest


@ExperimentalCoroutinesApi
abstract class TradeViewModelTest : BaseViewModelTest() {

    @Mock
    protected lateinit var tradeRepository: TradeRepository

    @InjectMocks
    protected lateinit var tradeViewModel: TradeViewModel

    @Mock
    protected lateinit var candleRepository: CandleRepository

    @Before
    fun setUp() {

    }

    /*@Test
    @DisplayName("Fetch Trades")
    fun shouldCallTradeRepositoryWhenFetchTradesIsCalled() {
        tradeViewModel.fetchTrades("", false)
        tradeViewModel.tradeLiveData.disposeObserver()
        runBlocking {
            Mockito.verify(tradeRepository).fetchTrades(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())
            Mockito.verifyNoMoreInteractions(tradeRepository)
        }
    }

    @Test
    @DisplayName("Fetch Candles")
    fun shouldCallCandleRepositoryWhenFetchCandlesIsCalled() {
        candleViewModel.fetchCandles("", "", 0)
        candleViewModel.candleLiveData.disposeObserver()
        runBlocking {
            Mockito.verify(candleRepository).fetchCandles(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
            Mockito.verifyNoMoreInteractions(candleRepository)
        }
    }

    @Test
    @DisplayName("Fetch Trades")
    fun shouldReturnTradesResponseWhenFetchTradesIsCalled() {
        val response: Resource<List<Trade>> = Resource.success(listOf(Trade(), Trade()))

        runBlockingTest {
            Mockito.`when`(tradeRepository.fetchTrades(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())).thenReturn(response)
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
            Mockito.`when`(candleRepository.fetchCandles(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(response)
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
    }*/
}