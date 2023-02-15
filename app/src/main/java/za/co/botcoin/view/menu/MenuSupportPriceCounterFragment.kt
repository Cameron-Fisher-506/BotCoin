package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import za.co.botcoin.R
import za.co.botcoin.databinding.SupportPriceCounterFragmentBinding
import za.co.botcoin.utils.ConstantUtils

class MenuSupportPriceCounterFragment : MenuBaseFragment(R.layout.support_price_counter_fragment) {
    private lateinit var binding: SupportPriceCounterFragmentBinding
    private val menuSupportPriceCounterViewModel by viewModels<MenuSupportPriceCounterViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SupportPriceCounterFragmentBinding.bind(view)
        setUpViews()
        setUpOnClickListeners()
    }

    private fun setUpViews() {
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.support_price_counter_items, android.R.layout.simple_spinner_item)
        this.binding.spinner.adapter = adapter

        val supportPriceCounter = menuSupportPriceCounterViewModel.getSavedSupportPriceCounter()
        if (!supportPriceCounter.isNullOrBlank()) {
            this.binding.spinner.setSelection(supportPriceCounter.toInt())
        } else {
            this.binding.spinner.setSelection(if (ConstantUtils.supportPriceCounter > 0) ConstantUtils.supportPriceCounter - 1 else 0)
        }
    }

    private fun setUpOnClickListeners() {
        binding.saveButton.setOnClickListener {
            ConstantUtils.supportPriceCounter = this.binding.spinner.selectedItem.toString().toInt()
            menuSupportPriceCounterViewModel.saveSupportPriceCounter((this.binding.spinner.selectedItemPosition).toString())
            menuSupportPriceCounterViewModel.displaySavedToast()
        }
        binding.supportPriceCounterInformationView.setOnClickListener {
            menuSupportPriceCounterViewModel.displaySupportPriceCounterDescriptionAlertDialog()
        }
    }
}