package za.co.botcoin.view.home

import androidx.lifecycle.ViewModel
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.DISCLAIMER_ACCEPTANCE
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService

class HomeDisclaimerPolicyViewModel(
    private val sharedPreferencesService: ISharedPreferencesService
) : ViewModel() {
    companion object {
        private const val ACCEPTED = "true"
    }

    fun saveDisclaimerAcceptance() {
        sharedPreferencesService.save(DISCLAIMER_ACCEPTANCE, ACCEPTED)
    }
}