package za.co.botcoin.view.wallet

import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.disposeObserver
import za.co.botcoin.state.ServiceState
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.repository.balance.BalanceRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.view.BaseViewModelTest

@ExperimentalCoroutinesApi
class WalletViewModelTest : BaseViewModelTest() {
    @Mock
    private lateinit var alertDialogService: IAlertDialogService

    @Mock
    private lateinit var resourceManager: IResourceManager

    @Mock
    private lateinit var balanceRepository: BalanceRepository

    @InjectMocks
    private lateinit var walletViewModel: WalletViewModel

    @BeforeEach
    fun setUp() {
        walletViewModel.ioDispatcher = unconfinedTestDispatcher
    }

    @Test
    @DisplayName("Fetch Balances Behavioural Verification")
    fun shouldCallBalanceRepositoryWhenFetchBalancesIsCalled() {
        walletViewModel.fetchBalances()
        walletViewModel.balancesResponse.disposeObserver()
        runBlocking {
            Mockito.verify(balanceRepository).fetchBalances()
            Mockito.verifyNoMoreInteractions(balanceRepository)
        }
    }

    @Test
    @DisplayName("Fetch Balances State Verification")
    fun shouldReturnBalanceResponseWhenFetchBalancesIsCalled() {
        val balances: Resource<List<Balance>> = Resource.success(listOf(Balance()))

        runTest {
            Mockito.`when`(balanceRepository.fetchBalances()).thenReturn(balances)
        }

        walletViewModel.fetchBalances()
        with(walletViewModel.balancesResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(ServiceState.Loading, this?.serviceState)
        }

        with(walletViewModel.balancesResponse.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(ServiceState.Success, this?.serviceState)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }

    /*
    @Test
    @DisplayName("Post Order")
    fun shouldCallPostOrderRepositoryWhenPostOrderIsCalled() {
        postOrderViewModel.postOrder("", "", "", "")
        postOrderViewModel.postOrderLiveData.disposeObserver()
        runBlocking {
            Mockito.verify(postOrderRepository).postOrder(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
            Mockito.verifyNoMoreInteractions(postOrderRepository)
        }
    }

    @Test
    @DisplayName("Post Order")
    fun shouldReturnPostOrderResponseWhenPostOrderIsCalled() {
        val postOrder: Resource<List<PostOrder>> = Resource.success(listOf(PostOrder()))

        runBlockingTest {
            Mockito.`when`(postOrderRepository.postOrder(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(postOrder)
        }

        postOrderViewModel.postOrder("", "", "", "")
        with(postOrderViewModel.postOrderLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.LOADING, this?.status)
        }

        with(postOrderViewModel.postOrderLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assertTrue(!this?.data.isNullOrEmpty())
        }
    }*/
}