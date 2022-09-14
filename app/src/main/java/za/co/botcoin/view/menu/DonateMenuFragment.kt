package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.DonateMenuFragmentBinding
import za.co.botcoin.utils.ConstantUtils

class DonateMenuFragment : MenuBaseFragment(R.layout.donate_menu_fragment) {
    private lateinit var binding: DonateMenuFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DonateMenuFragmentBinding.bind(view)
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.donateBtcOptionLinearLayoutCompat.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ConstantUtils.BTC)
            Navigation.findNavController(it).navigate(action)
        }
        binding.donateXrpOptionLinearLayoutCompat.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ConstantUtils.XRP)
            Navigation.findNavController(it).navigate(action)
        }
        binding.donateEthOptionLinearLayout.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ConstantUtils.ETH)
            Navigation.findNavController(it).navigate(action)
        }
        binding.donateLtcOptionLinearLayout.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ConstantUtils.LTC)
            Navigation.findNavController(it).navigate(action)
        }
    }
}