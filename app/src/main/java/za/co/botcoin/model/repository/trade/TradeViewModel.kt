package za.co.botcoin.model.repository.trade

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import za.co.botcoin.model.models.Trade
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class TradeViewModel(application: Application) : BaseViewModel(application) {
    var repository: TradeRepository = TradeRepository(application)

    lateinit var tradeLiveData: LiveData<Resource<List<Trade>>>

    fun fetchTrades(pair: String, sortDescending: Boolean) {
        tradeLiveData = liveData(Dispatchers.IO) {
            emit(Resource.loading())
            emit(repository.fetchTrades(pair, sortDescending))
        }
    }
}