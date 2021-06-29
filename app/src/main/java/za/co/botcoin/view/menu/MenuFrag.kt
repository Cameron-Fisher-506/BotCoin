package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.MenuFragmentBinding

class MenuFrag : Fragment(R.layout.menu_fragment) {
    private lateinit var binding: MenuFragmentBinding

    companion object {
        const val TITLE = "Menu"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = MenuFragmentBinding.bind(view)

        addLunoApiOptionListener()
        addSupportResistanceListener()
        addDonateListener()
        addPullOutPriceOptionListener()
        addLogcatOptionListener()
        addSetSupportPriceCounterOptionListener()
        addSetResistancePriceCounterOptionListener()
    }

    private fun addSetResistancePriceCounterOptionListener() {
        this.binding.linearLayoutSetResistancePriceCounter.setOnClickListener {
            val action = MenuFragDirections.actionMenuFragToResistancePriceCounterFrag()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addSetSupportPriceCounterOptionListener() {
        this.binding.linearLayoutSetSupportPriceCounter.setOnClickListener {
            val action = MenuFragDirections.actionMenuFragToSupportPriceCounterFrag()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addLogcatOptionListener() {
        this.binding.linearLayoutLogcat.setOnClickListener {
            val action = MenuFragDirections.actionMenuFragToLogcatFrag2()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addPullOutPriceOptionListener() {
        this.binding.linearLayoutSetPullOutPrice.setOnClickListener {
            val action = MenuFragDirections.actionMenuFragToTrailingStopFrag()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addLunoApiOptionListener() {
        this.binding.linearLayoutLunoApiOption.setOnClickListener {
            val action = MenuFragDirections.actionMenuFragToLunoApiFrag2()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addSupportResistanceListener() {
        this.binding.linearLayoutSupportResistanceOption.setOnClickListener {
            val action = MenuFragDirections.actionMenuFragToSupportResistanceFrag()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addDonateListener() {
        this.binding.linearLayoutDonateOption.setOnClickListener {
            val action = MenuFragDirections.actionMenuFragToDonateMenuFrag()
            Navigation.findNavController(it).navigate(action)
        }
    }
}