package za.co.botcoin.view.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import za.co.botcoin.model.repository.AccountRepository
import za.co.botcoin.view.home.TickersViewModel

@RunWith(MockitoJUnitRunner::class)
abstract class TickersViewModelTest {

    @Mock
    protected lateinit var accountRepository: AccountRepository

    protected lateinit var tickersViewModel: TickersViewModel

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        tickersViewModel = TickersViewModel(ApplicationProvider.getApplicationContext())
        //accountRepository = AccountRepository(ApplicationProvider.getApplicationContext())
        tickersViewModel.repository = accountRepository
    }
}