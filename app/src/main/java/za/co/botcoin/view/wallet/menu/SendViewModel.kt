package za.co.botcoin.view.wallet.menu

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.utils.Resource

class SendViewModel(application: Application, private val sendRepository: SendRepository): BaseViewModel(application) {
    lateinit var sendResponse: LiveData<Resource<List<Send>>>

    fun send(amount: String, currency: String, address: String, destinationTag: String = "") {
        sendResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(sendRepository.send(amount, currency, address, destinationTag))
        }
    }
}