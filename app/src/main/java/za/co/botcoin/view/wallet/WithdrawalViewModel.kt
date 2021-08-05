package za.co.botcoin.view.wallet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.*
import za.co.botcoin.model.repository.AccountRepository
import za.co.botcoin.model.repository.WithdrawalRepository
import za.co.botcoin.utils.Resource
import javax.crypto.SecretKey

class WithdrawalViewModel(application: Application) : AndroidViewModel(application) {
    private val accountRepository: AccountRepository = AccountRepository(application)
    private val withdrawalRepository: WithdrawalRepository = WithdrawalRepository(application)

    lateinit var withdrawalLiveData: LiveData<Resource<List<Withdrawal>>>
    lateinit var sendLiveData: LiveData<Resource<List<Send>>>
    lateinit var receiveLiveData: LiveData<Resource<List<Receive>>>
    lateinit var ordersLiveData: LiveData<Resource<List<Order>>>
    lateinit var stopOrderLiveData: LiveData<Resource<List<StopOrder>>>

    fun withdrawal(type: String, amount: String, beneficiaryId: String) {
        withdrawalLiveData = liveData { emit(withdrawalRepository.withdrawal(type, amount, beneficiaryId)) }
    }

    fun send(amount: String, currency: String, address: String, destinationTag: String = "") {
        sendLiveData = liveData { emit(withdrawalRepository.send(amount, currency, address, destinationTag)) }
    }

    fun receive(asset: String, keyId: String, secretKey: String) {
        receiveLiveData = liveData {
            emit(Resource.loading())
            emit(withdrawalRepository.receive(asset, keyId, secretKey))
        }
    }

    fun fetchOrders() {
        ordersLiveData = liveData {
            emit(Resource.loading())
            emit(accountRepository.fetchOrders())
        }
    }

    fun stopOrder(orderId: String) {
        stopOrderLiveData = liveData { emit(withdrawalRepository.stopOrder(orderId)) }
    }
}