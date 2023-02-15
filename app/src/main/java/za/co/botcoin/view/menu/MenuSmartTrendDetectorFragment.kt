package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import za.co.botcoin.R
import za.co.botcoin.databinding.SmartTrendDetectorFragmentBinding
import za.co.botcoin.utils.ConstantUtils

class MenuSmartTrendDetectorFragment : MenuBaseFragment(R.layout.smart_trend_detector_fragment) {
    private lateinit var binding: SmartTrendDetectorFragmentBinding
    private val menuSmartTrendDetectorViewModel by viewModels<MenuSmartTrendDetectorViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SmartTrendDetectorFragmentBinding.bind(view)
        setUpViews()
        setUpOnClickListeners()
    }

    private fun setUpViews() {
        this.binding.spinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.trailing_items, android.R.layout.simple_spinner_item)

        val smartTrendDetectorMargin = menuSmartTrendDetectorViewModel.getSavedSmartTrendDetectorMarginPrice()
        if (!smartTrendDetectorMargin.isNullOrBlank()) {
            this.binding.spinner.setSelection(if (smartTrendDetectorMargin.toInt() > 0) smartTrendDetectorMargin.toInt() - 1 else 0)
        } else {
            this.binding.spinner.setSelection(if (ConstantUtils.smartTrendDetectorMargin > 0) ConstantUtils.smartTrendDetectorMargin - 1 else 0)
        }
    }

    private fun setUpOnClickListeners() {
        binding.saveButton.setOnClickListener {
            ConstantUtils.smartTrendDetectorMargin = this.binding.spinner.selectedItem.toString().replace("%", "").toInt()
            menuSmartTrendDetectorViewModel.saveSmartTrendDetectorMarginPrice((this.binding.spinner.selectedItemPosition + 1).toString())
            menuSmartTrendDetectorViewModel.displaySavedToast()
        }
        binding.useDefaultButton.setOnClickListener {
            ConstantUtils.smartTrendDetectorMargin = 5
            menuSmartTrendDetectorViewModel.saveSmartTrendDetectorMarginPrice(ConstantUtils.smartTrendDetectorMargin.toString())
            this.binding.spinner.setSelection(0)
            menuSmartTrendDetectorViewModel.displayDefaultValueSet()
        }
        binding.smartTrendDetectorInformationView.setOnClickListener {
            menuSmartTrendDetectorViewModel.displaySmartTrendDetectorAlertDialog()
        }
    }
}