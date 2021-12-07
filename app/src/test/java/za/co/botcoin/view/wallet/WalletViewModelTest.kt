package za.co.botcoin.view.wallet

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
import za.co.botcoin.model.repository.balance.BalanceRepository
import za.co.botcoin.model.repository.balance.BalanceViewModel
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.model.repository.withdrawal.WithdrawalViewModel

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
abstract class WalletViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val initRule: MockitoRule = MockitoJUnit.rule()

    protected val balanceRepository: BalanceRepository = Mockito.mock(BalanceRepository::class.java)

    protected lateinit var balanceViewModel: BalanceViewModel

    protected val withdrawalRepository: WithdrawalRepository = Mockito.mock(WithdrawalRepository::class.java)

    protected lateinit var withdrawalViewModel: WithdrawalViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val application = RuntimeEnvironment.getApplication()

        balanceViewModel = BalanceViewModel(application)
        balanceViewModel.repository = balanceRepository

        withdrawalViewModel = WithdrawalViewModel(application)
        withdrawalViewModel.repository = withdrawalRepository
    }
}