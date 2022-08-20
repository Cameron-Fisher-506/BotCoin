package za.co.botcoin.model.repository.stopOrder

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineDispatcher
import za.co.botcoin.model.models.StopOrder
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class StopOrderViewModel(application: Application, private val ioDispatcher: CoroutineDispatcher) : BaseViewModel(application) {
    var repository: StopOrderRepository = StopOrderRepository(application)

    lateinit var stopOrderLiveData: LiveData<Resource<List<StopOrder>>>

    fun stopOrder(orderId: String) {
        stopOrderLiveData = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(repository.stopOrder(orderId))
        }
    }
}