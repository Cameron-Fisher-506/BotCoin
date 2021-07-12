package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.DonateMenuFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.view.menu.donate.DonateFrag

class DonateMenuFrag : Fragment(R.layout.donate_menu_fragment) {
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
            val action = DonateMenuFragDirections.actionDonateMenuFragToDonateFrag2(ConstantUtils.BTC)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addDonateXrpOptionListener() {
        this.binding.donateXrpOptionLinearLayoutCompat.setOnClickListener {
            val action = DonateMenuFragDirections.actionDonateMenuFragToDonateFrag2(ConstantUtils.XRP)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addDonateEthOptionListener() {
        this.binding.donateEthOptionLinearLayout.setOnClickListener {
            val action = DonateMenuFragDirections.actionDonateMenuFragToDonateFrag2(ConstantUtils.ETH)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addDonateLtcOptionListener() {
        this.binding.donateLtcOptionLinearLayout.setOnClickListener {
            val action = DonateMenuFragDirections.actionDonateMenuFragToDonateFrag2(ConstantUtils.LTC)
            Navigation.findNavController(it).navigate(action)
        }
    }
}