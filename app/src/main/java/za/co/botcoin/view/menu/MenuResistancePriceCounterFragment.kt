package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.ResistancePriceCounterFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService

class MenuResistancePriceCounterFragment : MenuBaseFragment(R.layout.resistance_price_counter_fragment) {
    private lateinit var binding: ResistancePriceCounterFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = ResistancePriceCounterFragmentBinding.bind(view)

        wireUI()
        setBtnSaveListener()
        setImgBtnResistancePriceCounterListener()
    }

    private fun wireUI() {
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.resistance_price_counter_items, android.R.layout.simple_spinner_item)
        this.binding.spinner.adapter = adapter

        val resistancePriceCounter = BaseSharedPreferencesService[requireContext(), BaseSharedPreferencesService.RESISTANCE_PRICE_COUNTER]
        if (!resistancePriceCounter.isNullOrBlank()) {
            this.binding.spinner.setSelection(resistancePriceCounter.toInt())
        } else {
            this.binding.spinner.setSelection(if (ConstantUtils.resistancePriceCounter > 0) ConstantUtils.resistancePriceCounter - 1 else 0)
        }
    }

    private fun setBtnSaveListener() {
        this.binding.saveButton.setOnClickListener {
            ConstantUtils.resistancePriceCounter = this.binding.spinner.selectedItem.toString().toInt()
            saveUserResistancePriceCounter(this.binding.spinner.selectedItemPosition)
            GeneralUtils.makeToast(context, "Saved!")
        }
    }

    private fun setImgBtnResistancePriceCounterListener() {
        this.binding.resistancePriceCounterImageButon.setOnClickListener {
            GeneralUtils.createAlertDialog(context, "Resistance Price Counter", """
         BotCoin uses the Resistance Price Counter, to sell at solid resistance price
         
         E.g.
         Resistance Price Counter: 5
         BotCoin keeps track of the number of hits each price gets. The highest price with the highest number of hits > 5 will be set as the resistance price.
         """.trimIndent(), false).show()
        }
    }

    private fun saveUserResistancePriceCounter(itemPosition: Int) {
        BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.RESISTANCE_PRICE_COUNTER, itemPosition.toString())
    }
}