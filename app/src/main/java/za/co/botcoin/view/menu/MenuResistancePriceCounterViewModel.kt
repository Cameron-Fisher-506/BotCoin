package za.co.botcoin.view.menu

import androidx.lifecycle.ViewModel
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.RESISTANCE_PRICE_COUNTER
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService

class MenuResistancePriceCounterViewModel(
    private val alertDialogService: IAlertDialogService,
    private val resourceManager: IResourceManager,
    private val sharedPreferencesService: ISharedPreferencesService
) : ViewModel() {
    fun displaySavedToast() {
        alertDialogService.makeToast(resourceManager.getString(R.string.menu_saved))
    }

    fun displayResistancePriceCounterDescriptionAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.resistance_price_counter)
            message = resourceManager.getString(R.string.menu_resistance_price_counter_description)
        })
    }

    fun getSavedResistancePriceCounter(): String? = sharedPreferencesService[RESISTANCE_PRICE_COUNTER]

    fun saveResistancePriceCounter(counter: String) = sharedPreferencesService.save(RESISTANCE_PRICE_COUNTER, counter)
}