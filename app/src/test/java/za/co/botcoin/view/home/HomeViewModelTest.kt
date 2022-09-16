package za.co.botcoin.view.home

import junit.framework.Assert
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import za.co.botcoin.disposeObserver
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.model.repository.account.AccountRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseViewModelTest() {

    @Mock
    private lateinit var tickersRepository: TickersRepository

    @Mock
    private lateinit var accountRepository: AccountRepository

    @InjectMocks
    private lateinit var homeViewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        homeViewModel.ioDispatcher = unconfinedTestDispatcher
    }


    @Test
    @DisplayName("Fetch Tickers Behaviour Verification")
    fun shouldCallTickersRepositoryWhenFetchTickersIsCalled() {
        homeViewModel.fetchTickers()
        homeViewModel.tickersResponse.disposeObserver()
        runBlocking {
            verify(tickersRepository).fetchTickers()
            verifyNoMoreInteractions(tickersRepository)
        }
    }

    @Test
    @DisplayName("Fetch Tickers State Verification")
    fun shouldReturnTickerResponseWhenFetchTickersIsCalled() {
        val tickers: Resource<List<Ticker>> = Resource.success(listOf(Ticker(), Ticker()))

        runTest {
            `when`(tickersRepository.fetchTickers()).thenReturn(tickers)
        }

        homeViewModel.fetchTickers()
        with(homeViewModel.tickersResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}