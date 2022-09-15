package za.co.botcoin.view.menu

import android.app.Application
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService.SUPPORT_PRICE_COUNTER
import za.co.botcoin.utils.services.sharePreferencesService.ISharedPreferencesService

class MenuSupportPriceCounterViewModel(
    application: Application,
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val sharedPreferencesService: ISharedPreferencesService
) : BaseViewModel(application) {
    fun displaySavedToast() {
        alertDialogService.makeToast(resourceManager.getString(R.string.menu_saved))
    }

    fun displaySupportPriceCounterDescriptionAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.support_price_counter)
            message = resourceManager.getString(R.string.menu_support_price_counter_description)
        })
    }

    fun getSavedSupportPriceCounter(): String? = sharedPreferencesService[SUPPORT_PRICE_COUNTER]

    fun saveSupportPriceCounter(counter: String) {
        sharedPreferencesService.save(SUPPORT_PRICE_COUNTER, counter)
    }
}