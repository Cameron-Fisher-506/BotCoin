package za.co.botcoin.view.home

import androidx.lifecycle.ViewModel
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService.PRIVACY_POLICY_ACCEPTANCE
import za.co.botcoin.utils.services.sharedPreferencesService.ISharedPreferencesService

class HomePrivacyPolicyViewModel(
    private val sharedPreferencesService: ISharedPreferencesService
) : ViewModel() {
    companion object {
        private const val ACCEPTED = "true"
    }

    fun savePrivacyPolicyAcceptance() {
        sharedPreferencesService.save(PRIVACY_POLICY_ACCEPTANCE, ACCEPTED)
    }
}