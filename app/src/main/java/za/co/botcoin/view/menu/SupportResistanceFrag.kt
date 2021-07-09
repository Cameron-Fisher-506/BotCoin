package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.SupportResistanceFragmentBinding
import za.co.botcoin.utils.GeneralUtils

class SupportResistanceFrag : Fragment(R.layout.support_resistance_fragment) {
    private lateinit var binding: SupportResistanceFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = SupportResistanceFragmentBinding.bind(view)
        addApplyListener()
    }

    private fun addApplyListener() {
        this.binding.btnApply.setOnClickListener {
            val supportPrice = this.binding.txtSupport.text.toString()
            val resistancePrice = this.binding.txtResistance.text.toString()
            if (supportPrice.isNotBlank() && resistancePrice.isNotBlank()) {
                /*try {
                    val jsonObjectSupportResistance = JSONObject()
                    jsonObjectSupportResistance.put("supportPrice", supportPrice)
                    jsonObjectSupportResistance.put("resistancePrice", resistancePrice)
                    context?.let { SharedPreferencesUtils.save(it, SharedPreferencesUtils.SUPPORT_RESISTANCE_PREF, jsonObjectSupportResistance) }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message}" +
                            "Method: SupportResistanceFrag - addApplyListener " +
                            "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                }
                GeneralUtils.makeToast(context, "Support and Resistance applied!")*/
            } else {
                GeneralUtils.createAlertDialog(context, "Support/Resistance", "Please enter a Support and Resistance price before applying the change!", false)?.show()
            }
        }
    }
}