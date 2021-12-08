package za.co.botcoin.view.trade

import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Trade
import za.co.botcoin.utils.Resource

@ExperimentalCoroutinesApi
@DisplayName("Trade ViewModel State Verification Test")
class TradeViewModelStateVerificationTest : TradeViewModelTest() {

    @Test
    @DisplayName("Fetch Trades")
    fun shouldReturnTradesResponseWhenFetchTradesIsCalled() {
        val response: Resource<List<Trade>> = Resource.success(listOf(Trade(), Trade()))

        runBlocking {
            Mockito.`when`(tradeRepository.fetchTrades(anyString(), anyBoolean())).thenReturn(response)
        }

        tradeViewModel.fetchTrades("", false)
        with(tradeViewModel.tradeLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}