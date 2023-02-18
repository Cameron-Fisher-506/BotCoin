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
        binding.donateBtcOptionOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(BTC))
        }
        binding.donateXrpOptionOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate( DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(XRP))
        }
        binding.donateEthOptionOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ETH))
        }
        binding.donateLtcOptionOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(LTC))
        }
    }
}