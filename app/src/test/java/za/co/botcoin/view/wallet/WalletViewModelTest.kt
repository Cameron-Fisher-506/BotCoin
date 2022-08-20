package za.co.botcoin.view.wallet

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.mockito.Mockito
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
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
abstract class WalletViewModelTest : BaseViewModelTest() {

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
    override fun setUp() {
        super.setUp()

        balanceViewModel = BalanceViewModel(application, testCoroutineDispatcher)
        balanceViewModel.repository = balanceRepository

        withdrawalViewModel = WithdrawalViewModel(application, testCoroutineDispatcher)
        withdrawalViewModel.repository = withdrawalRepository

        stopOrderViewModel = StopOrderViewModel(application, testCoroutineDispatcher)
        stopOrderViewModel.repository = stopOrderRepository

        orderViewModel = OrderViewModel(application, testCoroutineDispatcher)
        orderViewModel.repository = orderRepository

        receiveViewModel = ReceiveViewModel(application, testCoroutineDispatcher)
        receiveViewModel.repository = receiveRepository

        sendViewModel = SendViewModel(application, testCoroutineDispatcher)
        sendViewModel.repository = sendRepository

        postOrderViewModel = PostOrderViewModel(application, testCoroutineDispatcher)
        postOrderViewModel.repository = postOrderRepository
    }
}