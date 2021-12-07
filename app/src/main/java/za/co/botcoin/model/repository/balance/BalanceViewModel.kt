package za.co.botcoin.model.repository.balance

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class BalanceViewModel(application: Application) : BaseViewModel(application) {
    private val balanceRepository: BalanceRepository = BalanceRepository(application)

    lateinit var balancesLiveData: LiveData<Resource<List<Balance>>>

    fun fetchBalances() {
        balancesLiveData = liveData(Dispatchers.IO) {
            emit(Resource.loading())
            emit(balanceRepository.fetchBalances())
        }
    }
}