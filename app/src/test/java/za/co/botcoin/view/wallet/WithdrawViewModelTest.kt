package za.co.botcoin.view.wallet

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import za.co.botcoin.disposeObserver
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class WithdrawViewModelTest : BaseViewModelTest() {
    @Mock
    private lateinit var withdrawalRepository: WithdrawalRepository

    @InjectMocks
    private lateinit var withdrawViewModel: WithdrawViewModel

    @BeforeEach
    fun setUp() {
        withdrawViewModel.ioDispatcher = unconfinedTestDispatcher
    }

    @Test
    @DisplayName("Withdrawal")
    fun shouldCallWithdrawalRepositoryWhenWithdrawalIsCalled() {
        withdrawViewModel.withdrawal("", "", "")
        withdrawViewModel.withdrawalResponse.disposeObserver()
        runBlocking {
            verify(withdrawalRepository).withdrawal(anyString(), anyString(), anyString())
            verifyNoMoreInteractions(withdrawalRepository)
        }
    }
}