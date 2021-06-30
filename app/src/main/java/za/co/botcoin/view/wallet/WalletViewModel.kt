package za.co.botcoin.view.wallet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.repository.BalancesRepository
import za.co.botcoin.utils.Resource

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val balancesRepository: BalancesRepository = BalancesRepository(application)

    lateinit var balancesLiveData: LiveData<Resource<List<Balance>>>

    public fun fetchBalances(update: Boolean) {
        balancesLiveData = balancesRepository.fetchBalances(update)
    }
}