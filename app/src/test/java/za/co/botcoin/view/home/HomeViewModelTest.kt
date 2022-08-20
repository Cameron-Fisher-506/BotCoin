package za.co.botcoin.view.home

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import za.co.botcoin.model.repository.account.AccountRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
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
    override fun setUp() {
        super.setUp()
        homeViewModel.ioDispatcher = testCoroutineDispatcher
    }
}