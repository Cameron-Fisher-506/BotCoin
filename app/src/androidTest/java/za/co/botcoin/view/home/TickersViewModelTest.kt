package za.co.botcoin.view.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import za.co.botcoin.model.repository.account.AccountRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.model.repository.tickers.TickersViewModel

@RunWith(MockitoJUnitRunner::class)
abstract class TickersViewModelTest {

    @Mock
    protected lateinit var tickersRepository: TickersRepository

    protected lateinit var tickersViewModel: TickersViewModel

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        tickersViewModel = TickersViewModel(ApplicationProvider.getApplicationContext())
        //accountRepository = AccountRepository(ApplicationProvider.getApplicationContext())
        tickersViewModel.repository = tickersRepository
    }
}