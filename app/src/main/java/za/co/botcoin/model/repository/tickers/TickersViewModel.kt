package za.co.botcoin.model.repository.tickers

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.Resource

class TickersViewModel(application: Application) : BaseViewModel(application) {
    var repository: TickersRepository = TickersRepository(application)

    lateinit var tickersLiveData: LiveData<Resource<List<Ticker>>>

    fun fetchTickers() {
        tickersLiveData = liveData(Dispatchers.IO) { emit(repository.fetchTickers()) }
    }
}