package za.co.botcoin.view.trade

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import za.co.botcoin.disposeObserver

@ExperimentalCoroutinesApi
@DisplayName("Trade ViewModel Behaviour Verification Test")
class TradeViewModelBehaviourVerificationTest : TradeViewModelTest() {

    @Test
    @DisplayName("Fetch Trades")
    fun shouldCallTradeRepositoryWhenFetchTradesIsCalled() {
        tradeViewModel.fetchTrades("", false)
        tradeViewModel.tradeLiveData.disposeObserver()
        runBlocking {
            verify(tradeRepository).fetchTrades(anyString(), anyBoolean())
            verifyNoMoreInteractions(tradeRepository)
        }
    }

    @Test
    @DisplayName("Fetch Candles")
    fun shouldCallCandleRepositoryWhenFetchCandlesIsCalled() {
        candleViewModel.fetchCandles("", "", 0)
        candleViewModel.candleLiveData.disposeObserver()
        runBlocking {
            verify(candleRepository).fetchCandles(anyString(), anyString(), anyInt())
            verifyNoMoreInteractions(candleRepository)
        }
    }
}