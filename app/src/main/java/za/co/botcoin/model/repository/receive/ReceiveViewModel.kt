package za.co.botcoin.model.repository.receive

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.Receive
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class ReceiveViewModel(application: Application) : BaseViewModel(application) {
    var repository: ReceiveRepository = ReceiveRepository(application)

    lateinit var receiveLiveData: LiveData<Resource<List<Receive>>>

    fun receive(asset: String, keyId: String, secretKey: String) {
        receiveLiveData = liveData {
            emit(repository.receive(asset, keyId, secretKey))
        }
    }
}