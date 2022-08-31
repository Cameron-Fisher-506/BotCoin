package za.co.botcoin.view.wallet.menu

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
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class ReceiveViewModelTest: BaseViewModelTest() {
    @Mock
    private lateinit var receiveRepository: ReceiveRepository

    @InjectMocks
    private lateinit var receiveViewModel: ReceiveViewModel

    @BeforeEach
    fun setUp() {
        receiveViewModel.ioDispatcher = unconfinedTestDispatcher
    }

    @Test
    @DisplayName("Receive")
    fun shouldCallReceiveRepositoryWhenReceiveIsCalled() {
        receiveViewModel.receive("", "", "")
        receiveViewModel.receiveResponse.disposeObserver()
        runBlocking {
            verify(receiveRepository).receive(anyString(), anyString(), anyString())
            verifyNoMoreInteractions(receiveRepository)
        }
    }
}