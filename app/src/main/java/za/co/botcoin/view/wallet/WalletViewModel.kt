package za.co.botcoin.view.wallet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.repository.AccountRepository
import za.co.botcoin.utils.Resource

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val accountRepository: AccountRepository = AccountRepository(application)

    lateinit var balancesLiveData: LiveData<Resource<List<Balance>>>

    fun fetchBalances() {
        balancesLiveData = liveData {
            emit(Resource.loading())
            emit(accountRepository.fetchBalances())
        }
    }
}