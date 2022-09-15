package za.co.botcoin.view.menu.donate

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.Receive
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.utils.Resource

class DonateMenuDonateViewModel(application: Application, private val receiveRepository: ReceiveRepository, private val sendRepository: SendRepository): BaseViewModel(application) {
    lateinit var receiveResponse: LiveData<Resource<List<Receive>>>
    lateinit var sendResponse: LiveData<Resource<List<Send>>>

    fun receive(asset: String, keyId: String, secretKey: String) {
        receiveResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(receiveRepository.receive(asset, keyId, secretKey))
        }
    }

    fun send(amount: String, currency: String, address: String, destinationTag: String = "") {
        sendResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(sendRepository.send(amount, currency, address, destinationTag))
        }
    }
}