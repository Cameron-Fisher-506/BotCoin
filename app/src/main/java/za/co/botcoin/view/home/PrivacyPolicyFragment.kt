package za.co.botcoin.view.home

import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.PrivacyPolicyFragmentBinding
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService
import kotlin.system.exitProcess

class PrivacyPolicyFragment : HomeBaseFragment(R.layout.privacy_policy_fragment) {
    private lateinit var binding: PrivacyPolicyFragmentBinding
    private val privacyPolicyViewModel by viewModels<PrivacyPolicyViewModel>(factoryProducer = { homeActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = PrivacyPolicyFragmentBinding.bind(view)

        setUpViews()
        setUpOnClickListeners(view)
    }

    private fun setUpViews() {
        this.binding.privacyPolicyTextView.movementMethod = ScrollingMovementMethod()
        this.binding.privacyPolicyTextView.text = getString(R.string.home_privacy_policy)
    }

    private fun setUpOnClickListeners(view: View) {
        this.binding.exitButton.setOnClickListener {
            requireActivity().finishAffinity()
            exitProcess(0)
        }

        this.binding.acceptButton.setOnClickListener {
            privacyPolicyViewModel.savePrivacyPolicyAcceptance()
            val action = PrivacyPolicyFragmentDirections.actionPrivacyPolicyFragmentToDisclaimerPolicyFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }
}