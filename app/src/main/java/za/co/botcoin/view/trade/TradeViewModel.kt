package za.co.botcoin.view.trade

import android.app.Application
import androidx.lifecycle.LiveData
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.repository.BalancesRepository
import za.co.botcoin.utils.Resource

class TradeViewModel(application: Application) {
    private val balancesRepository: BalancesRepository = BalancesRepository(application)

    lateinit var balancesLiveData: LiveData<Resource<List<Balance>>>

    fun fetchBalances(update: Boolean) {
        balancesLiveData = balancesRepository.fetchBalances(update)
    }
}