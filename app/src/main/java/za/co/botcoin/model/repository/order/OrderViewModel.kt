package za.co.botcoin.model.repository.order

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import za.co.botcoin.model.models.Order
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class OrderViewModel(application: Application) : BaseViewModel(application) {
    var repository: OrderRepository = OrderRepository(application)

    lateinit var ordersLiveData: LiveData<Resource<List<Order>>>

    fun fetchOrders() {
        ordersLiveData = liveData(Dispatchers.IO) {
            emit(repository.fetchOrders())
        }
    }
}