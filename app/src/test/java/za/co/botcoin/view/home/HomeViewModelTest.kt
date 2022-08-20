package za.co.botcoin.view.home

import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import za.co.botcoin.disposeObserver
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.model.repository.account.AccountRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
abstract class HomeViewModelTest : BaseViewModelTest() {

    @Mock
    protected lateinit var tickersRepository: TickersRepository

    @Mock
    protected lateinit var accountRepository: AccountRepository

    @InjectMocks
    protected lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        homeViewModel.ioDispatcher = testCoroutineDispatcher
    }


    @Test
    @DisplayName("Fetch Tickers Behaviour Verification")
    fun shouldCallTickersRepositoryWhenFetchTickersIsCalled() {
        homeViewModel.fetchTickers()
        homeViewModel.tickersResponse.disposeObserver()
        runBlocking {
            Mockito.verify(tickersRepository).fetchTickers()
            Mockito.verifyNoMoreInteractions(tickersRepository)
        }
    }

    @Test
    @DisplayName("Fetch Tickers State Verification")
    fun shouldReturnTickerResponseWhenFetchTickersIsCalled() {
        val tickers: Resource<List<Ticker>> = Resource.success(listOf(Ticker(), Ticker()))

        runBlockingTest {
            Mockito.`when`(tickersRepository.fetchTickers()).thenReturn(tickers)
        }

        homeViewModel.fetchTickers()
        with(homeViewModel.tickersResponse.getOrAwaitValue()) {
            Assert.assertNotNull(this)
            Assert.assertEquals(Status.SUCCESS, this?.status)
            Assert.assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}