package za.co.botcoin.view.settings

import androidx.lifecycle.ViewModel
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService

class AutoTradeViewModel(
    private val resourceManager: IResourceManager,
    private val alertDialogService: IAlertDialogService,
    private val sharedPreferencesService: ISharedPreferencesService
) : ViewModel() {
}