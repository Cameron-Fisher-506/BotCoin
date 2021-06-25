package za.co.botcoin.menu.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.LogcatFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils

class LogcatFrag : Fragment(R.layout.logcat_fragment) {
    private lateinit var binding: LogcatFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = LogcatFragmentBinding.bind(view)

        displayLogcat()
    }

    private fun displayLogcat() {
        if (ConstantUtils.resistancePrices != null && ConstantUtils.resistancePrices.size > 0) {
            val prices = StringBuilder()
            for (i in ConstantUtils.resistancePrices.indices) {
                prices.append("${ConstantUtils.resistancePrices[i].price}, ${ConstantUtils.resistancePrices[i].counter}")
            }
            this.binding.txtLogcat.append("BotService - setResistancePrice " +
                    "ResistancePrices: $prices " +
                    "CreatedTime: ${GeneralUtils.currentDateTime}")
        }
        if (ConstantUtils.supportPrices != null && ConstantUtils.supportPrices.size > 0) {
            val prices = StringBuilder()
            for (i in ConstantUtils.supportPrices.indices) {
                prices.append("${ConstantUtils.supportPrices[i].price}, ${ConstantUtils.supportPrices[i].counter}")
            }
            this.binding.txtLogcat.append("BotService - setSupportPrice " +
                    "SupportPrices: $prices " +
                    "CreatedTime: ${GeneralUtils.currentDateTime}")
        }
    }
}