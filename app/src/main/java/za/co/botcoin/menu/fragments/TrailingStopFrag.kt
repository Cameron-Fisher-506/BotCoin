package za.co.botcoin.menu.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.SetPulloutPriceFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPreferencesUtils

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
            val jsonObject = SharedPreferencesUtils.get(context, SharedPreferencesUtils.PULLOUT_BID_PRICE_USER)
            var itemPosition: Int? = null
            if (jsonObject != null && jsonObject.has("itemPosition")) {
                itemPosition = jsonObject.getInt("itemPosition")
            }
            val adapter = ArrayAdapter.createFromResource(context!!, R.array.trailing_stop_items, android.R.layout.simple_spinner_item)
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
            ConstantUtils.trailingStop = this.binding.spinner.selectedItem.toString().replace("%", "").toInt()
            saveUserPullOutBidPrice(this.binding.spinner.selectedItemPosition)
            GeneralUtils.makeToast(context, "Saved!")
        }
    }

    private fun setBtnUseDefaultListener() {
        this.binding.btnUseDefault.setOnClickListener {
            try {
                if (SharedPreferencesUtils.get(context, SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT) != null) {
                    val jsonObject = SharedPreferencesUtils.get(context, SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT)
                    if (jsonObject != null && jsonObject.has(SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT)) {
                        ConstantUtils.trailingStop = jsonObject.getInt(SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT)
                        GeneralUtils.makeToast(context, "Default value set!")
                    } else {
                        GeneralUtils.makeToast(context, "Default value not found!")
                    }
                } else {
                    GeneralUtils.makeToast(context, "Default value not found!")
                }
            } catch (e: Exception) {
                Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                        "Method: MainActivity - saveDefaultPullOutBidPrice " +
                        "CreatedTime: ${GeneralUtils.currentDateTime}")
            }
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
     """.trimIndent(), false).show()
        }
    }

    private fun saveUserPullOutBidPrice(itemPosition: Int) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put(SharedPreferencesUtils.PULLOUT_BID_PRICE_USER, ConstantUtils.trailingStop)
            jsonObject.put("itemPosition", itemPosition)
            SharedPreferencesUtils.save(context, SharedPreferencesUtils.PULLOUT_BID_PRICE_USER, jsonObject)
        } catch (e: Exception) {
            Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                    "Method: MainActivity - saveDefaultPullOutBidPrice " +
                    "CreatedTime: ${GeneralUtils.currentDateTime}")
        }
    }
}