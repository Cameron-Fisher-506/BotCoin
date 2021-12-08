package za.co.botcoin.view.wallet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
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
import za.co.botcoin.model.repository.balance.BalanceRepository
import za.co.botcoin.model.repository.balance.BalanceViewModel
import za.co.botcoin.model.repository.order.OrderRepository
import za.co.botcoin.model.repository.order.OrderViewModel
import za.co.botcoin.model.repository.postOrder.PostOrderRepository
import za.co.botcoin.model.repository.postOrder.PostOrderViewModel
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.model.repository.receive.ReceiveViewModel
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.model.repository.send.SendViewModel
import za.co.botcoin.model.repository.stopOrder.StopOrderRepository
import za.co.botcoin.model.repository.stopOrder.StopOrderViewModel
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.model.repository.withdrawal.WithdrawalViewModel

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
abstract class WalletViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Rule
    @JvmField
    val initRule: MockitoRule = MockitoJUnit.rule()

    protected val balanceRepository: BalanceRepository = Mockito.mock(BalanceRepository::class.java)

    protected lateinit var balanceViewModel: BalanceViewModel

    protected val withdrawalRepository: WithdrawalRepository = Mockito.mock(WithdrawalRepository::class.java)

    protected lateinit var withdrawalViewModel: WithdrawalViewModel

    protected val stopOrderRepository: StopOrderRepository = Mockito.mock(StopOrderRepository::class.java)

    protected lateinit var stopOrderViewModel: StopOrderViewModel

    protected val orderRepository: OrderRepository = Mockito.mock(OrderRepository::class.java)

    protected lateinit var orderViewModel: OrderViewModel

    protected val receiveRepository: ReceiveRepository = Mockito.mock(ReceiveRepository::class.java)

    protected lateinit var receiveViewModel: ReceiveViewModel

    protected val sendRepository: SendRepository = Mockito.mock(SendRepository::class.java)

    protected lateinit var sendViewModel: SendViewModel

    protected val postOrderRepository: PostOrderRepository = Mockito.mock(PostOrderRepository::class.java)

    protected lateinit var postOrderViewModel: PostOrderViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        val application = RuntimeEnvironment.getApplication()

        balanceViewModel = BalanceViewModel(application)
        balanceViewModel.repository = balanceRepository

        withdrawalViewModel = WithdrawalViewModel(application)
        withdrawalViewModel.repository = withdrawalRepository

        stopOrderViewModel = StopOrderViewModel(application)
        stopOrderViewModel.repository = stopOrderRepository

        orderViewModel = OrderViewModel(application)
        orderViewModel.repository = orderRepository

        receiveViewModel = ReceiveViewModel(application)
        receiveViewModel.repository = receiveRepository

        sendViewModel = SendViewModel(application)
        sendViewModel.repository = sendRepository

        postOrderViewModel = PostOrderViewModel(application)
        postOrderViewModel.repository = postOrderRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}