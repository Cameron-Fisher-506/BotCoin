package za.co.botcoin.view.trade

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.mockito.Mockito
import za.co.botcoin.model.repository.candle.CandleRepository
import za.co.botcoin.model.repository.candle.CandleViewModel
import za.co.botcoin.model.repository.trade.TradeRepository
import za.co.botcoin.model.repository.trade.TradeViewModel
import za.co.botcoin.view.BaseViewModelTest


@ExperimentalCoroutinesApi
abstract class TradeViewModelTest : BaseViewModelTest() {

    protected val tradeRepository: TradeRepository = Mockito.mock(TradeRepository::class.java)

    protected lateinit var tradeViewModel: TradeViewModel

    protected val candleRepository: CandleRepository = Mockito.mock(CandleRepository::class.java)

    protected lateinit var candleViewModel: CandleViewModel

    @Before
    override fun setUp() {
        super.setUp()
        tradeViewModel = TradeViewModel(application, testCoroutineDispatcher)
        tradeViewModel.repository = tradeRepository

        candleViewModel = CandleViewModel(application, testCoroutineDispatcher)
        candleViewModel.repository = candleRepository
    }
}