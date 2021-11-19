package za.co.botcoin.view.home

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import za.co.botcoin.model.repository.AccountRepository

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
abstract class TickersViewModelTest {

    @Mock
    protected lateinit var accountRepository: AccountRepository

    @Mock
    protected lateinit var tickersViewModel: TickersViewModel

    @Before
    fun setUp() {
        val application = Mockito.mock(Application::class.java)
        //tickersViewModel = TickersViewModel(application)
        tickersViewModel.repository = accountRepository

    }
}