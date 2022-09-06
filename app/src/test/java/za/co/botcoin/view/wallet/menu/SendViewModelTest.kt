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
import za.co.botcoin.disposeObserver
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.utils.Resource
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
    @DisplayName("Send Behavioural Verification")
    fun shouldCallSendRepositoryWhenSendIsCalled() {
        sendViewModel.send("", "", "", "")
        sendViewModel.sendResponse.disposeObserver()
        runBlocking {
            verify(sendRepository).send(anyString(), anyString(), anyString(), anyString())
            verifyNoMoreInteractions(sendRepository)
        }
    }

    @Test
    @DisplayName("Send State Verification")
    fun shouldReturnSendResponseWhenSendIsCalled() {
        val send: Resource<List<Send>> = Resource.success(listOf(Send()))

        runTest {
            `when`(sendRepository.send(anyString(), anyString(), anyString(), anyString())).thenReturn(send)
        }

        sendViewModel.send("", "", "", "")
        with(sendViewModel.sendResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(sendViewModel.sendResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }
}