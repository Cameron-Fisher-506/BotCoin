package za.co.botcoin.view.wallet

import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import za.co.botcoin.disposeObserver
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.utils.Resource
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
    @DisplayName("Withdrawal Behavioural Verification")
    fun shouldCallWithdrawalRepositoryWhenWithdrawalIsCalled() {
        withdrawViewModel.withdrawal("", "", "")
        withdrawViewModel.withdrawalResponse.disposeObserver()
        runBlocking {
            verify(withdrawalRepository).withdrawal(anyString(), anyString(), anyString())
            verifyNoMoreInteractions(withdrawalRepository)
        }
    }

    @Test
    @DisplayName("Withdrawal State Verification")
    fun shouldReturnWithdrawalResponseWhenWithdrawalIsCalled() {
        val withdrawal: Resource<List<Withdrawal>> = Resource.success(listOf(Withdrawal()))

        runTest {
            Mockito.`when`(withdrawalRepository.withdrawal(anyString(), anyString(), anyString())).thenReturn(withdrawal)
        }

        withdrawViewModel.withdrawal("", "", "")
        with(withdrawViewModel.withdrawalResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(withdrawViewModel.withdrawalResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}