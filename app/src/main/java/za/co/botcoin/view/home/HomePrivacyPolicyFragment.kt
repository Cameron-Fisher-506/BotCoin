package za.co.botcoin.view.home

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.PrivacyPolicyFragmentBinding
import kotlin.system.exitProcess

class HomePrivacyPolicyFragment : HomeBaseFragment(R.layout.privacy_policy_fragment) {
    private lateinit var binding: PrivacyPolicyFragmentBinding
    private val privacyPolicyViewModel by viewModels<HomePrivacyPolicyViewModel>(factoryProducer = { homeActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PrivacyPolicyFragmentBinding.bind(view)

        setUpViews()
        setUpOnClickListeners(view)
    }

    private fun setUpViews() {
        binding.privacyPolicyTextView.movementMethod = ScrollingMovementMethod()
        binding.privacyPolicyTextView.text = getString(R.string.home_privacy_policy)
    }

    private fun setUpOnClickListeners(view: View) {
        binding.exitButton.setOnClickListener {
            requireActivity().finishAffinity()
            exitProcess(0)
        }

        binding.acceptButton.setOnClickListener {
            privacyPolicyViewModel.savePrivacyPolicyAcceptance()
            val action = HomePrivacyPolicyFragmentDirections.actionPrivacyPolicyFragmentToDisclaimerPolicyFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }
}