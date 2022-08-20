package za.co.botcoin.view.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.account.AccountRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.utils.Resource

class HomeViewModel(application: Application, private val accountRepository: AccountRepository, private val tickersRepository: TickersRepository) : BaseViewModel(application) {
    lateinit var tickersResponse: LiveData<Resource<List<Ticker>>>

    fun fetchTickers() {
        tickersResponse = liveData(ioDispatcher) {
            emit(tickersRepository.fetchTickers())
        }
    }
}