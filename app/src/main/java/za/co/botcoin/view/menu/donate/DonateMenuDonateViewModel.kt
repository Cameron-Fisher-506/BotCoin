package za.co.botcoin.view.menu.donate

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.models.Receive
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.model.repository.send.SendRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.clipBoardService.IClipBoardService
import za.co.botcoin.utils.services.notificationService.INotificationService

class DonateMenuDonateViewModel(
    application: Application,
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val notificationService: INotificationService,
    private val clipBoardService: IClipBoardService,
    private val receiveRepository: ReceiveRepository,
    private val sendRepository: SendRepository
) : BaseViewModel(application) {
    lateinit var receiveResponse: LiveData<Resource<List<Receive>>>
    lateinit var sendResponse: LiveData<Resource<List<Send>>>

    fun receive(asset: String, keyId: String, secretKey: String) {
        receiveResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(receiveRepository.receive(asset, keyId, secretKey))
        }
    }

    fun send(amount: String, currency: String, address: String, destinationTag: String = "") {
        sendResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(sendRepository.send(amount, currency, address, destinationTag))
        }
    }

    fun displayInvalidAmountEnteredAlertDialog(asset: String) {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.menu_invalid_amount_entered)
            message = resourceManager.getString(R.string.menu_please_note_you_cannot_donate_zero, asset)
        })
    }

    fun displayLunoApiCredentialsAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.menu_luno_api_credentials)
            message = resourceManager.getString(R.string.menu_please_set_your_luno_api_credentials)
        })
    }

    fun displayDonateDescriptionAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.donate)
            message = resourceManager.getString(R.string.donate_description)
        })
    }

    fun copyToClipBoard(data: String) = clipBoardService.copyToClipBoard(data)

    fun displaySendFailedNotification() = notificationService.notify(resourceManager.getString(R.string.menu_send_failed), resourceManager.getString(R.string.menu_send_failed))

    fun displaySentAmountOfAssetToAddressNotification(amount: String, asset: String, address: String) {
        notificationService.notify(resourceManager.getString(R.string.menu_sent_amount_to_address, amount, asset, address), "")
    }
}