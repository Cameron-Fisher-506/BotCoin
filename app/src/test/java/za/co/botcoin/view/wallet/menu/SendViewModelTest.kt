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
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class SendViewModelTest: BaseViewModelTest() {
    @Mock
    private lateinit var sendRepository: SendRepository

    @InjectMocks
    private lateinit var sendViewModel: SendViewModel

    @BeforeEach
    fun setUp() {
        sendViewModel.ioDispatcher = unconfinedTestDispatcher
    }

    @Test
    @DisplayName("Send")
    fun shouldCallSendRepositoryWhenSendIsCalled() {
        sendViewModel.send("", "", "", "")
        sendViewModel.sendResponse.disposeObserver()
        runBlocking {
            verify(sendRepository).send(anyString(), anyString(), anyString(), anyString())
            verifyNoMoreInteractions(sendRepository)
        }
    }
}