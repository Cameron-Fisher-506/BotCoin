package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.MenuFragmentBinding

class MenuFragment : MenuBaseFragment(R.layout.menu_fragment) {
    private lateinit var binding: MenuFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MenuFragmentBinding.bind(view)
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.setResistancePriceCounterLinearLayoutCompat.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToResistancePriceCounterFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.setSupportPriceCounterLinearLayoutCompat.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToSupportPriceCounterFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.trailingStartLinearLayoutCompat.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToTrailingStartFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.trailingStopLinearLayoutCompat.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToTrailingStopFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.lunoApiOptionLinearLayoutCompat.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToLunoApiFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.donateOptionLinearLayoutCompat.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToDonateMenuFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.setSmartTrendDetectorMarginLinearLayoutCompat.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToSmartTrendDetectorFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
}