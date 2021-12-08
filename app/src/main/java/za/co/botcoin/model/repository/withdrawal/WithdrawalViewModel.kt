package za.co.botcoin.model.repository.withdrawal

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class WithdrawalViewModel(application: Application): BaseViewModel(application) {
    var repository: WithdrawalRepository = WithdrawalRepository(application)

    lateinit var withdrawalLiveData: LiveData<Resource<List<Withdrawal>>>

    fun withdrawal(type: String, amount: String, beneficiaryId: String) {
        withdrawalLiveData = liveData(Dispatchers.IO) { emit(repository.withdrawal(type, amount, beneficiaryId)) }
    }
}