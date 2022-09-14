package za.co.botcoin.view.menu

import android.app.Application
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService.TRAILING_STOP
import za.co.botcoin.utils.services.sharePreferencesService.ISharedPreferencesService

class MenuTrailingStopViewModel(
    application: Application,
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val sharedPreferencesService: ISharedPreferencesService
) : BaseViewModel(application) {
    fun displaySavedToast() {
        alertDialogService.makeToast(resourceManager.getString(R.string.menu_saved))
    }

    fun displayDefaultValueSet() {
        alertDialogService.makeToast(resourceManager.getString(R.string.menu_default_value_set))
    }

    fun displayTrailingStopDescriptionAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.trailing_stop)
            message = resourceManager.getString(R.string.menu_trailing_stop_description)
        })
    }

    fun getSavedTrailingStop(): String? = sharedPreferencesService[TRAILING_STOP]
}