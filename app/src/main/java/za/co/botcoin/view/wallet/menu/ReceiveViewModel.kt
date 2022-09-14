package za.co.botcoin.view.wallet.menu

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.models.Receive
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.model.repository.receive.ReceiveRepository
import za.co.botcoin.utils.Resource
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService

class ReceiveViewModel(
    application: Application,
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val receiveRepository: ReceiveRepository
) : BaseViewModel(application) {
    lateinit var receiveResponse: LiveData<Resource<List<Receive>>>

    fun receive(asset: String, keyId: String, secretKey: String) {
        receiveResponse = liveData(ioDispatcher) {
            emit(Resource.loading())
            emit(receiveRepository.receive(asset, keyId, secretKey))
        }
    }
}