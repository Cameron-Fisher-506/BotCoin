package za.co.botcoin.view.home

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.DisclaimerPolicyFragmentBinding
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService
import kotlin.system.exitProcess

class DisclaimerPolicyFragment : Fragment(R.layout.disclaimer_policy_fragment) {
    private lateinit var binding: DisclaimerPolicyFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = DisclaimerPolicyFragmentBinding.bind(view)

        this.binding.disclaimerPolicyTextView.movementMethod = ScrollingMovementMethod()
        this.binding.disclaimerPolicyTextView.text = getString(R.string.home_disclaimer_policy)

        this.binding.exitButton.setOnClickListener {
            requireActivity().finishAffinity()
            exitProcess(0)
        }

        this.binding.acceptButton.setOnClickListener {
            BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.DISCLAIMER_ACCEPTANCE, "true")
            val action = DisclaimerPolicyFragmentDirections.actionDisclaimerPolicyFragmentToHomeFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }
}