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
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.disposeObserver
import za.co.botcoin.state.ServiceState
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.notificationService.INotificationService
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class WalletWithdrawViewModelTest : BaseViewModelTest() {
    @Mock
    private lateinit var alertDialogService: IAlertDialogService

    @Mock
    private lateinit var resourceManager: IResourceManager

    @Mock
    private lateinit var notificationService: INotificationService

    @Mock
    private lateinit var withdrawalRepository: WithdrawalRepository

    @InjectMocks
    private lateinit var walletWithdrawViewModel: WalletWithdrawViewModel

    @BeforeEach
    fun setUp() {
        walletWithdrawViewModel.ioDispatcher = unconfinedTestDispatcher
    }

    @Test
    @DisplayName("Withdrawal Behavioural Verification")
    fun shouldCallWithdrawalRepositoryWhenWithdrawalIsCalled() {
        walletWithdrawViewModel.withdrawal("", "", "")
        walletWithdrawViewModel.withdrawalResponse.disposeObserver()
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

        walletWithdrawViewModel.withdrawal("", "", "")
        with(walletWithdrawViewModel.withdrawalResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(ServiceState.Loading, this?.serviceState)
        }

        with(walletWithdrawViewModel.withdrawalResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(ServiceState.Success, this?.serviceState)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}