package za.co.botcoin.model.repository.postOrder

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import za.co.botcoin.model.models.PostOrder
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class PostOrderViewModel(application: Application) : BaseViewModel(application) {
    var repository: PostOrderRepository = PostOrderRepository(application)

    lateinit var postOrderLiveData: LiveData<Resource<List<PostOrder>>>

    fun postOrder(pair: String, type: String, volume: String, price: String) {
        postOrderLiveData = liveData(Dispatchers.IO) {
            emit(Resource.loading())
            emit(repository.postOrder(pair, type, volume, price))
        }
    }
}