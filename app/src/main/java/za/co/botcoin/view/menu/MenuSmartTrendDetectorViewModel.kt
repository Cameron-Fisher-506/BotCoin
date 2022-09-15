package za.co.botcoin.view.menu

import android.app.Application
import za.co.botcoin.R
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.model.repository.BaseViewModel
import za.co.botcoin.utils.services.alertDialogService.AlertDialogProperties
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService.SMART_TREND_DETECTOR
import za.co.botcoin.utils.services.sharePreferencesService.ISharedPreferencesService

class MenuSmartTrendDetectorViewModel(
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