package za.co.botcoin.model.repository.candle

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineDispatcher
import za.co.botcoin.model.models.Candle
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class CandleViewModel(application: Application) : BaseViewModel(application) {
    var repository: CandleRepository = CandleRepository(application)

    lateinit var candleLiveData: LiveData<Resource<List<Candle>>>

    fun fetchCandles(pair: String, since: String, duration: Int) {
        candleLiveData = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(repository.fetchCandles(pair, since, duration))
        }
    }
}