package za.co.botcoin.view.wallet.menu

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
import org.mockito.Mockito.*
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.disposeObserver
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Receive
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class WalletMenuReceiveViewModelTest: BaseViewModelTest() {
    @Mock
    private lateinit var alertDialogService: IAlertDialogService

    @Mock
    private lateinit var resourceManager: IResourceManager

    @Mock
    private lateinit var receiveRepository: ReceiveRepository

    @InjectMocks
    private lateinit var walletMenuReceiveViewModel: WalletMenuReceiveViewModel

    @BeforeEach
    fun setUp() {
        walletMenuReceiveViewModel.ioDispatcher = unconfinedTestDispatcher
    }

    @Test
    @DisplayName("Receive Behavioural Verification")
    fun shouldCallReceiveRepositoryWhenReceiveIsCalled() {
        walletMenuReceiveViewModel.receive("", "", "")
        walletMenuReceiveViewModel.receiveResponse.disposeObserver()
        runBlocking {
            verify(receiveRepository).receive(anyString(), anyString(), anyString())
            verifyNoMoreInteractions(receiveRepository)
        }
    }

    @Test
    @DisplayName("Receive State Verification")
    fun shouldReturnReceiveResponseWhenReceiveIsCalled() {
        val receive: Resource<List<Receive>> = Resource.success(listOf(Receive()))

        runTest {
            `when`(receiveRepository.receive(anyString(), anyString(), anyString())).thenReturn(receive)
        }

        walletMenuReceiveViewModel.receive("", "", "")
        with(walletMenuReceiveViewModel.receiveResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(walletMenuReceiveViewModel.receiveResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}