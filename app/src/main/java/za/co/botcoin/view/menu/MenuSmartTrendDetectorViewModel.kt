package za.co.botcoin.view.menu

import androidx.lifecycle.ViewModel
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.SMART_TREND_DETECTOR
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService

class MenuSmartTrendDetectorViewModel(
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

    fun displaySmartTrendDetectorAlertDialog() {
        alertDialogService.showAlertDialog(AlertDialogProperties().apply {
            title = resourceManager.getString(R.string.smart_trend_detector)
            message = resourceManager.getString(R.string.smart_trend_detector)
        })
    }

    fun getSavedSmartTrendDetectorMarginPrice(): String? = sharedPreferencesService[SMART_TREND_DETECTOR]

    fun saveSmartTrendDetectorMarginPrice(price: String) {
        sharedPreferencesService.save(SMART_TREND_DETECTOR, price)
    }
}