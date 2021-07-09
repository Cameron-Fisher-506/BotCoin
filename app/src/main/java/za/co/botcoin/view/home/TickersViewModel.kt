package za.co.botcoin.view.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.model.repository.AccountRepository
import za.co.botcoin.utils.Resource

class TickersViewModel(application: Application): AndroidViewModel(application) {
    private val accountRepository: AccountRepository = AccountRepository(application)

    lateinit var tickersLiveData: LiveData<Resource<List<Ticker>>>

    init {
        AccountRepository(application)
    }

    fun fetchTickers() {
        tickersLiveData = liveData(Dispatchers.IO) { emit(accountRepository.fetchTickers()) }
    }
}