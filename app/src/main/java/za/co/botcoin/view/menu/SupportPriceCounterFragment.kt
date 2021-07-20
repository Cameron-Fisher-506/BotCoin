package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.SupportPriceCounterFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPrefsUtils

class SupportPriceCounterFragment : Fragment(R.layout.support_price_counter_fragment) {
    private lateinit var binding: SupportPriceCounterFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = SupportPriceCounterFragmentBinding.bind(view)
        wireUI()
        setBtnSaveListener()
        setImgBtnSupportPriceCounterListener()
    }

    private fun wireUI() {
        val adapter = context?.let { ArrayAdapter.createFromResource(it, R.array.support_price_counter_items, android.R.layout.simple_spinner_item) }
        this.binding.spinner.adapter = adapter

        val supportPriceCounter = context?.let { SharedPrefsUtils[it, SharedPrefsUtils.SUPPORT_PRICE_COUNTER] }
        if (supportPriceCounter != null) {
            this.binding.spinner.setSelection(supportPriceCounter.toInt())
        }
    }

    private fun setBtnSaveListener() {
        this.binding.saveButton.setOnClickListener {
            ConstantUtils.supportPriceCounter = this.binding.spinner.selectedItem.toString().toInt()
            saveUserSupportPriceCounter(this.binding.spinner.selectedItemPosition)
            GeneralUtils.makeToast(context, "Saved!")
        }
    }

    private fun setImgBtnSupportPriceCounterListener() {
        this.binding.supportPriceCounterImageButton.setOnClickListener {
            GeneralUtils.createAlertDialog(context, "Support Price Counter", """
     BotCoin uses the Support Price Counter, to buy at solid support price
     
     E.g.
     Support Price Counter: 5
     BotCoin keeps track of the number of hits each price gets. The lowest price with the highest number of hits > 5 will be set as the support price.
     """.trimIndent(), false).show()
        }
    }

    private fun saveUserSupportPriceCounter(itemPosition: Int) {
        context?.let { SharedPrefsUtils.save(it, SharedPrefsUtils.SUPPORT_PRICE_COUNTER, itemPosition.toString()) }
    }
}