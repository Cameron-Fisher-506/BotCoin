package za.co.botcoin.view.wallet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.balance.BalanceRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService

class WalletViewModel(
    application: Application,
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val balanceRepository: BalanceRepository
) : BaseViewModel(application) {
    lateinit var balancesResponse: LiveData<Resource<List<Balance>>>

    companion object {
        const val XRP = "XRP"
        const val ZAR = "ZAR"
    }

    fun fetchBalances() {
        balancesResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(balanceRepository.fetchBalances())
        }
    }

    fun displayLunoApiCredentialsAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.wallet_luno_api_credentials)
            message = resourceManager.getString(R.string.wallet_please_set_your_luno_api_credentials)
        })
    }
}