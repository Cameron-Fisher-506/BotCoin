package za.co.botcoin.view.menu.donate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.Receive
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.repository.WithdrawalRepository
import za.co.botcoin.utils.Resource

class DonateViewModel(application: Application) : AndroidViewModel(application) {
    private val withdrawalRepository: WithdrawalRepository = WithdrawalRepository(application)

    lateinit var receiveLiveData: LiveData<Resource<List<Receive>>>
    lateinit var sendLiveData: LiveData<Resource<List<Send>>>

    fun receive(asset: String, keyId: String, secretKey: String) {
        receiveLiveData = liveData {
            emit(Resource.loading())
            emit(withdrawalRepository.receive(asset, keyId, secretKey))
        }
    }

    fun send(amount: String, currency: String, address: String, destinationTag: String = "") {
        sendLiveData = liveData { emit(withdrawalRepository.send(amount, currency, address, destinationTag)) }
    }
}