package za.co.botcoin.menu.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.ResistancePriceCounterFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPreferencesUtils

class ResistancePriceCounterFrag : Fragment(R.layout.resistance_price_counter_fragment) {
    private lateinit var binding: ResistancePriceCounterFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = ResistancePriceCounterFragmentBinding.bind(view)

        wireUI()
        setBtnSaveListener()
        setImgBtnResistancePriceCounterListener()
    }

    private fun wireUI() {
        try {
            val jsonObject = SharedPreferencesUtils.get(context, SharedPreferencesUtils.RESISTANCE_PRICE_COUNTER)
            var itemPosition: Int? = null

            if (jsonObject != null && jsonObject.has("itemPosition")) {
                itemPosition = jsonObject.getInt("itemPosition")
            }

            val adapter = ArrayAdapter.createFromResource(context!!, R.array.resistance_price_counter_items, android.R.layout.simple_spinner_item)
            this.binding.spinner.adapter = adapter
            if (itemPosition != null) {
                this.binding.spinner.setSelection(itemPosition)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setBtnSaveListener() {
        this.binding.btnSave.setOnClickListener {
            ConstantUtils.supportPriceCounter = this.binding.spinner.selectedItem.toString().toInt()
            saveUserResistancePriceCounter(this.binding.spinner.selectedItemPosition)
            GeneralUtils.makeToast(context, "Saved!")
        }
    }

    private fun setImgBtnResistancePriceCounterListener() {
        this.binding.imgBtnResistancePriceCounter.setOnClickListener {
            GeneralUtils.createAlertDialog(context, "Resistance Price Counter", """
     BotCoin uses the Resistance Price Counter, to sell at solid resistance price
     
     E.g.
     Resistance Price Counter: 5
     BotCoin keeps track of the number of hits each price gets. The highest price with the highest number of hits > 5 will be set as the resistance price.
     """.trimIndent(), false).show()
        }
    }

    private fun saveUserResistancePriceCounter(itemPosition: Int) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put(SharedPreferencesUtils.RESISTANCE_PRICE_COUNTER, ConstantUtils.resistancePriceCounter)
            jsonObject.put("itemPosition", itemPosition)
            SharedPreferencesUtils.save(context, SharedPreferencesUtils.RESISTANCE_PRICE_COUNTER, jsonObject)
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: MainActivity - saveUserResistancePriceCounter " +
                    "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
        }
    }
}