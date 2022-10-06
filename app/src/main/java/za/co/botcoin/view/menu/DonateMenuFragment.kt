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

    companion object {
        private const val BTC = "XBT"
        private const val ETH = "ETH"
        private const val LTC = "LTC"
        private const val XRP = "XRP"
        private const val ZAR = "ZAR"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DonateMenuFragmentBinding.bind(view)
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.donateBtcOptionLinearLayoutCompat.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(BTC)
            Navigation.findNavController(it).navigate(action)
        }
        binding.donateXrpOptionLinearLayoutCompat.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(XRP)
            Navigation.findNavController(it).navigate(action)
        }
        binding.donateEthOptionLinearLayout.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ETH)
            Navigation.findNavController(it).navigate(action)
        }
        binding.donateLtcOptionLinearLayout.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(LTC)
            Navigation.findNavController(it).navigate(action)
        }
    }
}