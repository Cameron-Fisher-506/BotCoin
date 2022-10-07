package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import za.co.botcoin.R
import za.co.botcoin.databinding.TrailingStopFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService

class MenuTrailingStopFragment : MenuBaseFragment(R.layout.trailing_stop_fragment) {
    private lateinit var binding: TrailingStopFragmentBinding
    private val menuTrailingStopViewModel by viewModels<MenuTrailingStopViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = TrailingStopFragmentBinding.bind(view)
        setUpViews()
        setUpOnClickListeners()
    }

    private fun setUpViews() {
        this.binding.spinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.trailing_items, android.R.layout.simple_spinner_item)

        val trailingStop = menuTrailingStopViewModel.getSavedTrailingStop()
        if (!trailingStop.isNullOrBlank()) {
            this.binding.spinner.setSelection(if (trailingStop.toInt() > 0) trailingStop.toInt() - 1 else 0)
        } else {
            this.binding.spinner.setSelection(if (ConstantUtils.trailingStop > 0) ConstantUtils.trailingStop - 1 else 0)
        }
    }

    private fun setUpOnClickListeners() {
        this.binding.saveButton.setOnClickListener {
            ConstantUtils.trailingStop = this.binding.spinner.selectedItem.toString().replace("%", "").toInt()
            menuTrailingStopViewModel.saveTrailingStop((binding.spinner.selectedItemPosition + 1).toString())
            menuTrailingStopViewModel.displaySavedToast()
        }

        this.binding.useDefaultButton.setOnClickListener {
            ConstantUtils.trailingStop = 10
            BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.TRAILING_STOP, ConstantUtils.trailingStop.toString())
            this.binding.spinner.setSelection(0)
            menuTrailingStopViewModel.displayDefaultValueSet()
        }

        this.binding.trailingStopImageButton.setOnClickListener {
            menuTrailingStopViewModel.displayTrailingStopDescriptionAlertDialog()
        }
    }
}