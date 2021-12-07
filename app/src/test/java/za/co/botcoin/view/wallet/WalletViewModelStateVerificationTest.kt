package za.co.botcoin.view.wallet

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito
import za.co.botcoin.enum.Status
import za.co.botcoin.getOrAwaitValue
import za.co.botcoin.model.models.Balance
import za.co.botcoin.utils.Resource

class WalletViewModelStateVerificationTest : WalletViewModelTest() {

    @Test
    fun shouldReturnBalanceResponseWhenFetchBalancesIsCalled() {
        val balances: Resource<List<Balance>> = Resource(Status.SUCCESS, listOf(Balance(), Balance()), "Success")

        runBlocking {
            Mockito.`when`(balanceRepository.fetchBalances()).thenReturn(balances)
        }

        balanceViewModel.fetchBalances()
        with(balanceViewModel.balancesLiveData.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(Status.SUCCESS, this?.status)
            assert(!this?.data.isNullOrEmpty())
        }
    }
}