package za.co.botcoin.view.home

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.mockito.Mockito
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.model.repository.tickers.TickersViewModel
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
abstract class HomeViewModelTest : BaseViewModelTest() {

    protected val tickersRepository: TickersRepository = Mockito.mock(TickersRepository::class.java)

    protected lateinit var tickersViewModel: TickersViewModel

    @Before
    override fun setUp() {
        super.setUp()
        tickersViewModel = TickersViewModel(application)
        tickersViewModel.repository = tickersRepository
    }
}