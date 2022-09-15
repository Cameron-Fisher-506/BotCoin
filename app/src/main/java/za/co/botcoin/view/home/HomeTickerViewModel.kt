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
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService
import za.co.botcoin.utils.services.sharePreferencesService.ISharedPreferencesService

class HomeTickerViewModel(
    application: Application,
    private val sharedPreferencesService: ISharedPreferencesService,
    private val accountRepository: AccountRepository,
    private val tickersRepository: TickersRepository
) : BaseViewModel(application) {
    lateinit var tickersResponse: LiveData<Resource<List<Ticker>>>

    fun fetchTickers() {
        tickersResponse = liveData(ioDispatcher) {
            emit(tickersRepository.fetchTickers())
        }
    }

    fun setUserTrailingStartPrice() {
        val trailingStart = sharedPreferencesService[BaseSharedPreferencesService.TRAILING_START]
        if (!trailingStart.isNullOrBlank()) {
            ConstantUtils.trailingStart = trailingStart.toInt()
        }
    }

    fun setUserTrailingStopPrice() {
        val trailingStop = sharedPreferencesService[BaseSharedPreferencesService.TRAILING_STOP]
        if (!trailingStop.isNullOrBlank()) {
            ConstantUtils.trailingStop = trailingStop.toInt()
        }
    }

    fun setSupportPriceCounter() {
        val supportPriceCounter = sharedPreferencesService[BaseSharedPreferencesService.SUPPORT_PRICE_COUNTER]
        if (!supportPriceCounter.isNullOrBlank()) {
            ConstantUtils.supportPriceCounter = supportPriceCounter.toInt()
        }
    }

    fun setResistancePriceCounter() {
        val resistancePriceCounter = sharedPreferencesService[BaseSharedPreferencesService.RESISTANCE_PRICE_COUNTER]
        if (!resistancePriceCounter.isNullOrBlank()) {
            ConstantUtils.resistancePriceCounter = resistancePriceCounter.toInt()
        }
    }

    fun setSmartTrendDetectorMargin() {
        val smartTrendDetectorMargin = sharedPreferencesService[BaseSharedPreferencesService.SMART_TREND_DETECTOR]
        if (!smartTrendDetectorMargin.isNullOrBlank()) {
            ConstantUtils.smartTrendDetectorMargin = smartTrendDetectorMargin.toInt()
        }
    }

    fun getPrivacyPolicyAcceptance(): String? = sharedPreferencesService[BaseSharedPreferencesService.PRIVACY_POLICY_ACCEPTANCE]
}