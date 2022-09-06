package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.TrailingStartFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.SharedPreferencesService

class TrailingStartFragment : Fragment(R.layout.trailing_start_fragment) {
    private lateinit var binding: TrailingStartFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = TrailingStartFragmentBinding.bind(view)
        wireUI()
        setBtnSaveListener()
        setImgBtnTrailingStartListener()
        setBtnUseDefaultListener()
    }

    private fun wireUI() {
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.trailing_items, android.R.layout.simple_spinner_item)
        this.binding.spinner.adapter = adapter

        val trailingStart = SharedPreferencesService[requireContext(), SharedPreferencesService.TRAILING_START]
        if (!trailingStart.isNullOrBlank()) {
            this.binding.spinner.setSelection(if (trailingStart.toInt() > 0) trailingStart.toInt() - 1 else 0)
        } else {
            this.binding.spinner.setSelection(if (ConstantUtils.trailingStart > 0) ConstantUtils.trailingStart - 1 else 0)
        }
    }

    private fun setBtnSaveListener() {
        this.binding.saveButton.setOnClickListener {
            ConstantUtils.trailingStart = this.binding.spinner.selectedItem.toString().replace("%", "").toInt()
            saveUserPullOutBidPrice(this.binding.spinner.selectedItemPosition + 1)
            GeneralUtils.makeToast(context, "Saved!")
        }
    }

    private fun setBtnUseDefaultListener() {
        this.binding.useDefaultButton.setOnClickListener {
            ConstantUtils.trailingStart = 5
            SharedPreferencesService.save(requireContext(), SharedPreferencesService.TRAILING_START, ConstantUtils.trailingStart.toString())
            this.binding.spinner.setSelection(0)
            GeneralUtils.makeToast(context, "Default value set!")
        }
    }

    private fun setImgBtnTrailingStartListener() {
        this.binding.trailingStartImageButton.setOnClickListener {
            GeneralUtils.createAlertDialog(context, "Trailing Start", """
         BotCoin uses the trailing start percentage, to start trading again if the market is in an upward trend.
         
         E.g.
         Trailing start: 10%
         Current price: R100
         Buy order: R109.99
         
         In the above scenario, BotCoin will create a buy order if the price increases 10% above the last lowest support price.
         BotCoin will always place a buy order of R0.1 less than the current price.
         """.trimIndent(), false).show()
        }
    }

    private fun saveUserPullOutBidPrice(trailingStart: Int) {
        SharedPreferencesService.save(requireContext(), SharedPreferencesService.TRAILING_START, trailingStart.toString())
    }
}