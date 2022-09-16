package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import za.co.botcoin.R
import za.co.botcoin.databinding.ResistancePriceCounterFragmentBinding
import za.co.botcoin.utils.ConstantUtils

class MenuResistancePriceCounterFragment : MenuBaseFragment(R.layout.resistance_price_counter_fragment) {
    private lateinit var binding: ResistancePriceCounterFragmentBinding
    private val menuResistancePriceCounterViewModel by viewModels<MenuResistancePriceCounterViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ResistancePriceCounterFragmentBinding.bind(view)

        setUpViews()
        setUpOnClickListeners()
    }

    private fun setUpViews() {
        this.binding.spinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.resistance_price_counter_items, android.R.layout.simple_spinner_item)

        val resistancePriceCounter = menuResistancePriceCounterViewModel.getSavedResistancePriceCounter()
        if (!resistancePriceCounter.isNullOrBlank()) {
            this.binding.spinner.setSelection(resistancePriceCounter.toInt())
        } else {
            this.binding.spinner.setSelection(if (ConstantUtils.resistancePriceCounter > 0) ConstantUtils.resistancePriceCounter - 1 else 0)
        }
    }

    private fun setUpOnClickListeners() {
        this.binding.saveButton.setOnClickListener {
            ConstantUtils.resistancePriceCounter = this.binding.spinner.selectedItem.toString().toInt()
            menuResistancePriceCounterViewModel.saveResistancePriceCounter((this.binding.spinner.selectedItemPosition).toString())
            menuResistancePriceCounterViewModel.displaySavedToast()
        }
        this.binding.resistancePriceCounterImageButon.setOnClickListener {
           menuResistancePriceCounterViewModel.displayResistancePriceCounterDescriptionAlertDialog()
        }
    }
}