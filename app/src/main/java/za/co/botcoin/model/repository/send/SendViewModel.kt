package za.co.botcoin.model.repository.send

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineDispatcher
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class SendViewModel(application: Application) : BaseViewModel(application) {
    var repository: SendRepository = SendRepository(application)

    lateinit var sendLiveData: LiveData<Resource<List<Send>>>

    fun send(amount: String, currency: String, address: String, destinationTag: String = "") {
        sendLiveData = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(repository.send(amount, currency, address, destinationTag))
        }
    }
}