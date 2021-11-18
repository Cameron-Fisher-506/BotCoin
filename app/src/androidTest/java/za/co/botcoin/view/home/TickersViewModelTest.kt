package za.co.botcoin.view.home

import android.app.Application
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import za.co.botcoin.model.repository.AccountRepository

@RunWith(MockitoJUnitRunner::class)
abstract class TickersViewModelTest {

    @Mock
    protected lateinit var accountRepository: AccountRepository

    protected lateinit var tickersViewModel: TickersViewModel

    @Before
    fun setUp() {
        val application = Mockito.mock(Application::class.java)
        tickersViewModel = TickersViewModel(application)
        tickersViewModel.repository = accountRepository

    }
}