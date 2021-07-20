package za.co.ticker.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import za.co.ticker.R
import za.co.ticker.databinding.DonateMenuFragmentBinding
import za.co.ticker.utils.ConstantUtils

class DonateMenuFragment : Fragment(R.layout.donate_menu_fragment) {
    private lateinit var binding: DonateMenuFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = DonateMenuFragmentBinding.bind(view)

        addDonateBtcOptionListener()
        addDonateEthOptionListener()
        addDonateLtcOptionListener()
        addDonateXrpOptionListener()
    }

    private fun addDonateBtcOptionListener() {
        this.binding.donateBtcOptionLinearLayoutCompat.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ConstantUtils.BTC)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addDonateXrpOptionListener() {
        this.binding.donateXrpOptionLinearLayoutCompat.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ConstantUtils.XRP)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addDonateEthOptionListener() {
        this.binding.donateEthOptionLinearLayout.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ConstantUtils.ETH)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addDonateLtcOptionListener() {
        this.binding.donateLtcOptionLinearLayout.setOnClickListener {
            val action = DonateMenuFragmentDirections.actionDonateMenuFragmentToDonateFragment(ConstantUtils.LTC)
            Navigation.findNavController(it).navigate(action)
        }
    }
}