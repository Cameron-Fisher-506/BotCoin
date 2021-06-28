package za.co.botcoin.navigation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import za.co.botcoin.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.MenuFragmentBinding
import za.co.botcoin.menu.fragments.*
import za.co.botcoin.utils.FragmentUtils

class MenuFrag : Fragment() {
    private lateinit var binding: MenuFragmentBinding

    companion object {
        const val TITLE = "Menu"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = MenuFragmentBinding.bind(view)

        addLunoApiOptionListener(view.findViewById(R.id.linearLayoutLunoApiOption))
        addSupportResistanceListener(view.findViewById(R.id.linearLayoutSupportResistanceOption))
        addDonateListener(view.findViewById(R.id.linearLayoutDonateOption))
        addPullOutPriceOptionListener(view.findViewById(R.id.linearLayoutSetPullOutPrice))
        addLogcatOptionListener(view.findViewById(R.id.linearLayoutLogcat))
        addSetSupportPriceCounterOptionListener(view.findViewById(R.id.linearLayoutSetSupportPriceCounter))
        addSetResistancePriceCounterOptionListener(view.findViewById(R.id.linearLayoutSetResistancePriceCounter))
    }

    private fun addSetResistancePriceCounterOptionListener(view: View) {
        this.binding.linearLayoutSetResistancePriceCounter.setOnClickListener {
            val resistancePriceCounterFrag = ResistancePriceCounterFrag()
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, resistancePriceCounterFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Set Resistance Price Counter", true, false,
                //true, null)
        }
    }

    private fun addSetSupportPriceCounterOptionListener(view: View) {

        this.binding.linearLayoutSetSupportPriceCounter.setOnClickListener {
            val supportPriceCounterFrag = SupportPriceCounterFrag()
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, supportPriceCounterFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Set Support Price Counter", true, false, true,
                //null)
        }
    }

    private fun addLogcatOptionListener(view: View) {
        this.binding.linearLayoutLogcat.setOnClickListener {
            val logcatFrag = LogcatFrag()
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, logcatFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Logcat", true, false, true, null)
        }
    }

    private fun addPullOutPriceOptionListener(view: View) {
        this.binding.linearLayoutSetPullOutPrice.setOnClickListener {
            val setPullOutPriceFrag = TrailingStopFrag()
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, setPullOutPriceFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Set Pull-out Price", true, false, true, null)
        }
    }

    private fun addLunoApiOptionListener(view: View) {
        this.binding.linearLayoutLunoApiOption.setOnClickListener {
            val lunoApiFrag = LunoApiFrag()
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, lunoApiFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Luno API", true, false, true, null)
        }
    }

    private fun addSupportResistanceListener(view: View) {
        this.binding.linearLayoutSupportResistanceOption.setOnClickListener {
            val supportResistanceFrag = SupportResistanceFrag()
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, supportResistanceFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Support/Resistance", true, false, true, null)
        }
    }

    private fun addDonateListener(view: View) {
        this.binding.linearLayoutDonateOption.setOnClickListener {
            val donateFrag = DonateMenuFrag()
            //FragmentUtils.startFragment((activity as MainActivity?)!!.supportFragmentManager, donateFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Donate Menu", true, false, true, null)
        }
    }
}