package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import za.co.botcoin.R
import za.co.botcoin.databinding.TrailingStartFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.services.sharedPreferencesService.BaseSharedPreferencesService

class MenuTrailingStartFragment : MenuBaseFragment(R.layout.trailing_start_fragment) {
    private lateinit var binding: TrailingStartFragmentBinding
    private val menuTrailingStartViewModel by viewModels<MenuTrailingStartViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = TrailingStartFragmentBinding.bind(view)
        setUpViews()
        setUpOnClickListeners()
    }

    private fun setUpViews() {
        this.binding.spinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.trailing_items, android.R.layout.simple_spinner_item)

        val trailingStart = menuTrailingStartViewModel.getSavedTrailingStart()
        if (!trailingStart.isNullOrBlank()) {
            this.binding.spinner.setSelection(if (trailingStart.toInt() > 0) trailingStart.toInt() - 1 else 0)
        } else {
            this.binding.spinner.setSelection(if (ConstantUtils.trailingStart > 0) ConstantUtils.trailingStart - 1 else 0)
        }
    }

    private fun setUpOnClickListeners() {
        this.binding.saveButton.setOnClickListener {
            ConstantUtils.trailingStart = this.binding.spinner.selectedItem.toString().replace("%", "").toInt()
            menuTrailingStartViewModel.saveTrailingStart((this.binding.spinner.selectedItemPosition + 1).toString())
            menuTrailingStartViewModel.displaySavedToast()
        }

        this.binding.useDefaultButton.setOnClickListener {
            ConstantUtils.trailingStart = 5
            BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.TRAILING_START, ConstantUtils.trailingStart.toString())
            this.binding.spinner.setSelection(0)
            menuTrailingStartViewModel.displayDefaultValueSet()
        }

        this.binding.trailingStartImageButton.setOnClickListener {
            menuTrailingStartViewModel.displayTrailingStartDescriptionAlertDialog()
        }
    }
}