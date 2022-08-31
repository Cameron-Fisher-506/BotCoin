package za.co.botcoin.view.wallet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.balance.BalanceRepository
import za.co.botcoin.utils.Resource

class WalletViewModel(application: Application, private val balanceRepository: BalanceRepository) : BaseViewModel(application) {
    lateinit var balancesResponse: LiveData<Resource<List<Balance>>>

    fun fetchBalances() {
        balancesResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(balanceRepository.fetchBalances())
        }
    }
}