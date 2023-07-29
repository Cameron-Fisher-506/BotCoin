package za.co.botcoin.view.home

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.DisclaimerPolicyFragmentBinding
import kotlin.system.exitProcess

class HomeDisclaimerPolicyFragment : HomeBaseFragment(R.layout.disclaimer_policy_fragment) {
    private lateinit var binding: DisclaimerPolicyFragmentBinding
    private val disclaimerViewModel by viewModels<HomeDisclaimerPolicyViewModel>(factoryProducer = { homeActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DisclaimerPolicyFragmentBinding.bind(view)

        setUpViews()
        setUpOnClickListeners(view)
    }

    private fun setUpViews() {
        binding.disclaimerPolicyTextView.movementMethod = ScrollingMovementMethod()
        binding.disclaimerPolicyTextView.text = getString(R.string.home_disclaimer_policy)
    }

    private fun setUpOnClickListeners(view: View) {
        binding.exitButton.setOnClickListener {
            requireActivity().finishAffinity()
            exitProcess(0)
        }

        binding.acceptButton.setOnClickListener {
            disclaimerViewModel.saveDisclaimerAcceptance()
            val action = HomeDisclaimerPolicyFragmentDirections.actionDisclaimerPolicyFragmentToHomeTickerFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }
}