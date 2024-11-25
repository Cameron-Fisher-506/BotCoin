package za.co.botcoin.view.wallet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.withdrawal.WithdrawalRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.notificationService.INotificationService

class WalletWithdrawViewModel(
    application: Application,
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val notificationService: INotificationService,
    private val withdrawalRepository: WithdrawalRepository
) : BaseViewModel(application) {
    lateinit var withdrawalResponse: LiveData<Resource<List<Withdrawal>>>

    companion object {
        const val ZAR_EFT = "ZAR_EFT"
    }

    fun withdrawal(type: String, amount: String, beneficiaryId: String) {
        withdrawalResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(withdrawalRepository.withdrawal(type, amount, beneficiaryId))
        }
    }

    fun displayWithdrawalAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.wallet_withdrawal)
            message = resourceManager.getString(R.string.wallet_please_provide_an_amount_more_than)
        })
    }

    fun displayWithdrawalDescriptionAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.wallet_withdrawal)
            message = resourceManager.getString(R.string.withdraw_description)
        })
    }

    fun isAmountNotEmptyAndNotZero(amount: String): Boolean = amount.isNotEmpty() && amount != "0"

    fun displayWithdrawalFailedNotification() = notificationService.notify(resourceManager.getString(R.string.wallet_withdrawal_failed), "")

    fun displayAmountWithdrewNotification(amount: String) = notificationService.notify("${ resourceManager.getString(R.string.wallet_withdrew) } R$amount", "")
}