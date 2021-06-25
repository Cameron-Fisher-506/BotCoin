package za.co.botcoin.menu.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.SupportPriceCounterFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPreferencesUtils

class SupportPriceCounterFrag : Fragment(R.layout.support_price_counter_fragment) {
    private lateinit var binding: SupportPriceCounterFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = SupportPriceCounterFragmentBinding.bind(view)
        wireUI()
        setBtnSaveListener()
        setImgBtnSupportPriceCounterListener()
    }

    private fun wireUI() {
        try {
            val jsonObject = SharedPreferencesUtils.get(context, SharedPreferencesUtils.SUPPORT_PRICE_COUNTER)
            var itemPosition: Int? = null
            if (jsonObject != null && jsonObject.has("itemPosition")) {
                itemPosition = jsonObject.getInt("itemPosition")
            }
            val adapter = ArrayAdapter.createFromResource(context!!, R.array.support_price_counter_items, android.R.layout.simple_spinner_item)
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
            saveUserSupportPriceCounter(this.binding.spinner.selectedItemPosition)
            GeneralUtils.makeToast(context, "Saved!")
        }
    }

    private fun setImgBtnSupportPriceCounterListener() {
        this.binding.imgBtnSupportPriceCounter.setOnClickListener {
            GeneralUtils.createAlertDialog(context, "Support Price Counter", """
     BotCoin uses the Support Price Counter, to buy at solid support price
     
     E.g.
     Support Price Counter: 5
     BotCoin keeps track of the number of hits each price gets. The lowest price with the highest number of hits > 5 will be set as the support price.
     """.trimIndent(), false).show()
        }
    }

    private fun saveUserSupportPriceCounter(itemPosition: Int) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put(SharedPreferencesUtils.SUPPORT_PRICE_COUNTER, ConstantUtils.supportPriceCounter)
            jsonObject.put("itemPosition", itemPosition)
            SharedPreferencesUtils.save(context, SharedPreferencesUtils.SUPPORT_PRICE_COUNTER, jsonObject)
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: MainActivity - saveUserSupportPriceCounter " +
                    "CreatedTime: ${GeneralUtils.currentDateTime}")
        }
    }
}