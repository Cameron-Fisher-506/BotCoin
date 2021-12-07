package za.co.botcoin.view.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.model.repository.tickers.TickersViewModel

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
abstract class TickersViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var initRule: MockitoRule = MockitoJUnit.rule()

    protected val tickersRepository: TickersRepository = Mockito.mock(TickersRepository::class.java)

    protected lateinit var tickersViewModel: TickersViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this);
        val application = RuntimeEnvironment.getApplication()
        tickersViewModel = TickersViewModel(application)
        tickersViewModel.repository = tickersRepository
    }
}