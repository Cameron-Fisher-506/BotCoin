package za.co.botcoin.view.wallet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.utils.Resource

class WithdrawViewModel(application: Application, private val withdrawalRepository: WithdrawalRepository): BaseViewModel(application) {
    lateinit var withdrawalResponse: LiveData<Resource<List<Withdrawal>>>

    fun withdrawal(type: String, amount: String, beneficiaryId: String) {
        withdrawalResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(withdrawalRepository.withdrawal(type, amount, beneficiaryId))
        }
    }
}