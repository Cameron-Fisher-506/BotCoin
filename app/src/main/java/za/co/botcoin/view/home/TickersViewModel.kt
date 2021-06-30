package za.co.botcoin.view.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.model.repository.LunoRepository
import za.co.botcoin.model.repository.TickersRepository
import za.co.botcoin.utils.Resource

class TickersViewModel(application: Application): AndroidViewModel(application) {
    private val tickersRepository: TickersRepository = TickersRepository(application)

    lateinit var tickersLiveData: LiveData<Resource<List<Ticker>>>

    init {
        LunoRepository(application)
    }

    fun fetchTickers(update: Boolean) {
        tickersLiveData = tickersRepository.fetchTickers(update)
    }
}