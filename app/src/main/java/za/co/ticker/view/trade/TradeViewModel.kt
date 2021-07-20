package za.co.ticker.view.trade

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.ticker.model.models.Balance
import za.co.ticker.model.repository.AccountRepository
import za.co.ticker.utils.Resource

class TradeViewModel(application: Application) {
    private val accountRepository: AccountRepository = AccountRepository(application)

    lateinit var balancesLiveData: LiveData<Resource<List<Balance>>>

    fun fetchBalances() {
        balancesLiveData = liveData { emit(accountRepository.fetchBalances()) }
    }
}