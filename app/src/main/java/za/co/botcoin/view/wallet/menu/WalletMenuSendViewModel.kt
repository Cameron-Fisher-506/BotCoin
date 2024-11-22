package za.co.botcoin.view.wallet.menu

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.AlertDialogService
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.notificationService.INotificationService

class WalletMenuSendViewModel(
    application: Application,
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val notificationService: INotificationService,
    private val sendRepository: SendRepository
) : BaseViewModel(application) {
    lateinit var sendResponse: LiveData<Resource<List<Send>>>

    fun send(amount: String, currency: String, address: String, destinationTag: String = "") {
        sendResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(sendRepository.send(amount, currency, address, destinationTag))
        }
    }

    fun displaySendDescriptionAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.send)
            message = resourceManager.getString(R.string.send_description)
        })
    }

    fun displaySendFailedNotification() = notificationService.notify(resourceManager.getString(R.string.wallet_send_failed), "")
}