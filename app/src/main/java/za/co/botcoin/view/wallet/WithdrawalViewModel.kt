package za.co.botcoin.view.wallet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.model.repository.WithdrawalRepository
import za.co.botcoin.utils.Resource

class WithdrawalViewModel(application: Application) : AndroidViewModel(application) {
    private val withdrawalRepository: WithdrawalRepository = WithdrawalRepository(application)

    lateinit var withdrawalLiveData: LiveData<Resource<List<Withdrawal>>>

    public fun withdrawal(mustWithdraw: Boolean, type: String, amount: String, beneficiaryId: String) {
        withdrawalLiveData = withdrawalRepository.withdrawal(mustWithdraw, type, amount, beneficiaryId)
    }
}