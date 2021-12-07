package za.co.botcoin.view.home

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.model.repository.tickers.TickersViewModel

@RunWith(MockitoJUnitRunner::class)
abstract class TickersViewModelTest {   

    protected val tickersRepository: TickersRepository = Mockito.mock(TickersRepository::class.java)

    private val application = Mockito.mock(Application::class.java)

    protected lateinit var tickersViewModel: TickersViewModel

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var initRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        tickersViewModel = TickersViewModel(application)
        tickersViewModel.repository = tickersRepository
    }
}