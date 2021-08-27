package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.MenuFragmentBinding

class MenuFragment : Fragment(R.layout.menu_fragment) {
    private lateinit var binding: MenuFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = MenuFragmentBinding.bind(view)

        addLunoApiOptionListener()
        addDonateListener()
        addTrailingStartOptionListener()
        addTrailingStopOptionListener()
        addSetSupportPriceCounterOptionListener()
        addSetResistancePriceCounterOptionListener()
        addSmartTrendDetectorMarginListener()
    }

    private fun addSetResistancePriceCounterOptionListener() {
        this.binding.linearLayoutSetResistancePriceCounter.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToResistancePriceCounterFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addSetSupportPriceCounterOptionListener() {
        this.binding.linearLayoutSetSupportPriceCounter.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToSupportPriceCounterFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addTrailingStartOptionListener() {
        this.binding.linearLayoutTrailingStart.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToTrailingStartFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addTrailingStopOptionListener() {
        this.binding.linearLayoutTrailingStop.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToTrailingStopFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addLunoApiOptionListener() {
        this.binding.linearLayoutLunoApiOption.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToLunoApiFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addDonateListener() {
        this.binding.linearLayoutDonateOption.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToDonateMenuFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addSmartTrendDetectorMarginListener() {
        this.binding.linearLayoutSetSmartTrendDetectorMargin.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToSmartTrendDetectorFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
}