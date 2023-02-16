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
            Navigation.findNavController(it).navigate(MenuFragmentDirections.actionMenuFragmentToResistancePriceCounterFragment())
        }
        binding.setSupportPriceCounterLinearLayoutCompat.setOnClickListener {
            Navigation.findNavController(it).navigate(MenuFragmentDirections.actionMenuFragmentToSupportPriceCounterFragment())
        }
        binding.trailingStartOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(MenuFragmentDirections.actionMenuFragmentToTrailingStartFragment())
        }
        binding.trailingStopOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(MenuFragmentDirections.actionMenuFragmentToTrailingStopFragment())
        }
        binding.lunoApiOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(MenuFragmentDirections.actionMenuFragmentToLunoApiFragment())
        }
        binding.donateOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(MenuFragmentDirections.actionMenuFragmentToDonateMenuFragment())
        }
        binding.setSmartTrendDetectorMarginLinearLayoutCompat.setOnClickListener {
            Navigation.findNavController(it).navigate(MenuFragmentDirections.actionMenuFragmentToSmartTrendDetectorFragment())
        }
    }
}