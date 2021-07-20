package za.co.ticker.view.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import za.co.ticker.model.models.Ticker
import za.co.ticker.model.repository.AccountRepository
import za.co.ticker.utils.Resource

class TickersViewModel(application: Application) : AndroidViewModel(application) {
    private val accountRepository: AccountRepository = AccountRepository(application)

    lateinit var tickersLiveData: LiveData<Resource<List<Ticker>>>

    init {
        AccountRepository(application)
    }

    fun fetchTickers() {
        tickersLiveData = liveData(Dispatchers.IO) { emit(accountRepository.fetchTickers()) }
    }
}