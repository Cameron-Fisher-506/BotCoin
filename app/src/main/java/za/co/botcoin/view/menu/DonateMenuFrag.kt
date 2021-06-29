package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
        this.binding.linearLayoutDonateBtcOption.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("asset", ConstantUtils.BTC)
            val donateFrag = DonateFrag()
            donateFrag.arguments = bundle
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, donateFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Donate BTC", true, false, true, null)
        }
    }

    private fun addDonateXrpOptionListener() {
        this.binding.linearLayoutDonateXrpOption.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("asset", ConstantUtils.XRP)
            val donateFrag = DonateFrag()
            donateFrag.arguments = bundle
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, donateFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Donate XRP", true, false, true, null)
        }
    }

    private fun addDonateEthOptionListener() {
        this.binding.linearLayoutDonateEthOption.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("asset", ConstantUtils.ETH)
            val donateFrag = DonateFrag()
            donateFrag.arguments = bundle
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, donateFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Donate ETH", true, false, true, null)
        }
    }

    private fun addDonateLtcOptionListener() {
        this.binding.linearLayoutDonateLtcOption.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("asset", ConstantUtils.LTC)
            val donateFrag = DonateFrag()
            donateFrag.arguments = bundle
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, donateFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Donate LTC", true, false, true, null)
        }
    }
}