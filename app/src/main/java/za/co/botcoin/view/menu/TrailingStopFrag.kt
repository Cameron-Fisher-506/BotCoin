package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.SetPulloutPriceFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPrefsUtils

class TrailingStopFrag : Fragment(R.layout.set_pullout_price_fragment) {
    private lateinit var binding: SetPulloutPriceFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = SetPulloutPriceFragmentBinding.bind(view)
        wireUI()
        setBtnSaveListener()
        setImgBtnTrailingStopListener()
        setBtnUseDefaultListener()
    }

    private fun wireUI() {
        try {
            val adapter = ArrayAdapter.createFromResource(context!!, R.array.trailing_stop_items, android.R.layout.simple_spinner_item)
            this.binding.spinner.adapter = adapter

            val trailingStop = context?.let { SharedPrefsUtils[it, SharedPrefsUtils.TRAILING_STOP] }
            if (trailingStop != null) {
                this.binding.spinner.setSelection(if (trailingStop.toInt() > 0) trailingStop.toInt()-1 else 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setBtnSaveListener() {
        this.binding.btnSave.setOnClickListener {
            ConstantUtils.trailingStop = this.binding.spinner.selectedItem.toString().replace("%", "").toInt()
            saveUserPullOutBidPrice(this.binding.spinner.selectedItemPosition + 1)
            GeneralUtils.makeToast(context, "Saved!")
        }
    }

    private fun setBtnUseDefaultListener() {
        this.binding.btnUseDefault.setOnClickListener {
            ConstantUtils.trailingStop = 1
            context?.let { SharedPrefsUtils.save(it, SharedPrefsUtils.TRAILING_STOP, ConstantUtils.trailingStop.toString()) }
            this.binding.spinner.setSelection(0)
            GeneralUtils.makeToast(context, "Default value set!")
        }
    }

    private fun setImgBtnTrailingStopListener() {
        this.binding.imgBtnTrailingStop.setOnClickListener {
            GeneralUtils.createAlertDialog(context, "Trailing Stop", """
     BotCoin uses the trailing stop percentage, to pullout of a trade if the market is in a downtrend.
     
     E.g.
     Trailing stop: 10%
     Current price: 100
     Sell order: 90
     
     In the above scenario BotCoin will create a sell order if the price drops 10% below than last highest resistance price.
     """.trimIndent(), false)?.show()
        }
    }

    private fun saveUserPullOutBidPrice(trailingStop: Int) {
        context?.let { SharedPrefsUtils.save(it, SharedPrefsUtils.TRAILING_STOP, trailingStop.toString()) }
    }
}