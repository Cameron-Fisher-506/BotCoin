package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.TrailingStopFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService

class TrailingStopFragment : Fragment(R.layout.trailing_stop_fragment) {
    private lateinit var binding: TrailingStopFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = TrailingStopFragmentBinding.bind(view)
        wireUI()
        setBtnSaveListener()
        setImgBtnTrailingStopListener()
        setBtnUseDefaultListener()
    }

    private fun wireUI() {
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.trailing_items, android.R.layout.simple_spinner_item)
        this.binding.spinner.adapter = adapter

        val trailingStop = BaseSharedPreferencesService[requireContext(), BaseSharedPreferencesService.TRAILING_STOP]
        if (!trailingStop.isNullOrBlank()) {
            this.binding.spinner.setSelection(if (trailingStop.toInt() > 0) trailingStop.toInt() - 1 else 0)
        } else {
            this.binding.spinner.setSelection(if (ConstantUtils.trailingStop > 0) ConstantUtils.trailingStop - 1 else 0)
        }
    }

    private fun setBtnSaveListener() {
        this.binding.saveButton.setOnClickListener {
            ConstantUtils.trailingStop = this.binding.spinner.selectedItem.toString().replace("%", "").toInt()
            saveUserPullOutBidPrice(this.binding.spinner.selectedItemPosition + 1)
            GeneralUtils.makeToast(context, "Saved!")
        }
    }

    private fun setBtnUseDefaultListener() {
        this.binding.useDefaultButton.setOnClickListener {
            ConstantUtils.trailingStop = 10
            BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.TRAILING_STOP, ConstantUtils.trailingStop.toString())
            this.binding.spinner.setSelection(0)
            GeneralUtils.makeToast(context, "Default value set!")
        }
    }

    private fun setImgBtnTrailingStopListener() {
        this.binding.trailingStopImageButton.setOnClickListener {
            GeneralUtils.createAlertDialog(context, "Trailing Stop", """
         BotCoin uses the trailing stop percentage, to pullout of a trade if the market is in a downtrend.
         
         E.g.
         Trailing stop: 10%
         Current price: 100
         Sell order: 90
         
         In the above scenario BotCoin will create a sell order if the price drops 10% below than last highest resistance price.
         """.trimIndent(), false).show()
        }
    }

    private fun saveUserPullOutBidPrice(trailingStop: Int) {
        BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.TRAILING_STOP, trailingStop.toString())
    }
}