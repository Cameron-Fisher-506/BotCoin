package za.co.botcoin.view.menu

import androidx.lifecycle.ViewModel
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.TRAILING_START
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService

class MenuTrailingStartViewModel(
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val sharedPreferencesService: ISharedPreferencesService
) : ViewModel() {
    fun displaySavedToast() {
        alertDialogService.makeToast(resourceManager.getString(R.string.menu_saved))
    }

    fun displayDefaultValueSet() {
        alertDialogService.makeToast(resourceManager.getString(R.string.menu_default_value_set))
    }

    fun displayTrailingStartDescriptionAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.trailing_start)
            message = resourceManager.getString(R.string.menu_trailing_start_description)
        })
    }

    fun getSavedTrailingStart(): String? = sharedPreferencesService[TRAILING_START]

    fun saveTrailingStart(trailingStart: String) {
        sharedPreferencesService.save(TRAILING_START, trailingStart)
    }
}