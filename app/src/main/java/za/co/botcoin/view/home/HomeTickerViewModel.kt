package za.co.botcoin.view.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.model.models.Ticker
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.account.AccountRepository
import za.co.botcoin.model.repository.tickers.TickersRepository
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.PRIVACY_POLICY_ACCEPTANCE
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.RESISTANCE_PRICE_COUNTER
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.SMART_TREND_DETECTOR
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.SUPPORT_PRICE_COUNTER
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.TRAILING_START
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.TRAILING_STOP
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService

class HomeTickerViewModel(
    application: Application,
    private val sharedPreferencesService: ISharedPreferencesService,
    private val accountRepository: AccountRepository,
    private val tickersRepository: TickersRepository
) : BaseViewModel(application) {
    lateinit var tickersResponse: LiveData<Resource<List<Ticker>>>

    companion object {
        const val PAIR_XRPZAR = "XRPZAR"
        const val DELAY = 5000L
    }

    fun fetchTickers() {
        tickersResponse = liveData(ioDispatcher) {
            emit(tickersRepository.fetchTickers())
        }
    }

    fun setUserTrailingStartPrice() {
        val trailingStart = sharedPreferencesService[TRAILING_START]
        if (!trailingStart.isNullOrBlank()) {
            ConstantUtils.trailingStart = trailingStart.toInt()
        }
    }

    fun setUserTrailingStopPrice() {
        val trailingStop = sharedPreferencesService[TRAILING_STOP]
        if (!trailingStop.isNullOrBlank()) {
            ConstantUtils.trailingStop = trailingStop.toInt()
        }
    }

    fun setSupportPriceCounter() {
        val supportPriceCounter = sharedPreferencesService[SUPPORT_PRICE_COUNTER]
        if (!supportPriceCounter.isNullOrBlank()) {
            ConstantUtils.supportPriceCounter = supportPriceCounter.toInt()
        }
    }

    fun setResistancePriceCounter() {
        val resistancePriceCounter = sharedPreferencesService[RESISTANCE_PRICE_COUNTER]
        if (!resistancePriceCounter.isNullOrBlank()) {
            ConstantUtils.resistancePriceCounter = resistancePriceCounter.toInt()
        }
    }

    fun setSmartTrendDetectorMargin() {
        val smartTrendDetectorMargin = sharedPreferencesService[SMART_TREND_DETECTOR]
        if (!smartTrendDetectorMargin.isNullOrBlank()) {
            ConstantUtils.smartTrendDetectorMargin = smartTrendDetectorMargin.toInt()
        }
    }

    fun getPrivacyPolicyAcceptance(): String? = sharedPreferencesService[PRIVACY_POLICY_ACCEPTANCE]
}