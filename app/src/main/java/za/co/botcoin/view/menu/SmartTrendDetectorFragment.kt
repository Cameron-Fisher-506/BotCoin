package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.SmartTrendDetectorFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService

class SmartTrendDetectorFragment : Fragment(R.layout.smart_trend_detector_fragment) {
    private lateinit var binding: SmartTrendDetectorFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = SmartTrendDetectorFragmentBinding.bind(view)
        wireUI()
        setBtnSaveListener()
        setImgBtnSmartTrendDectectorListener()
        setBtnUseDefaultListener()
    }

    private fun wireUI() {
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.trailing_items, android.R.layout.simple_spinner_item)
        this.binding.spinner.adapter = adapter

        val smartTrendDetectorMargin = BaseSharedPreferencesService[requireContext(), BaseSharedPreferencesService.SMART_TREND_DETECTOR]
        if (!smartTrendDetectorMargin.isNullOrBlank()) {
            this.binding.spinner.setSelection(if (smartTrendDetectorMargin.toInt() > 0) smartTrendDetectorMargin.toInt() - 1 else 0)
        } else {
            this.binding.spinner.setSelection(if (ConstantUtils.smartTrendDetectorMargin > 0) ConstantUtils.smartTrendDetectorMargin - 1 else 0)
        }
    }

    private fun setBtnSaveListener() {
        this.binding.saveButton.setOnClickListener {
            ConstantUtils.smartTrendDetectorMargin = this.binding.spinner.selectedItem.toString().replace("%", "").toInt()
            saveUserSmartTrendDectectorMarginPrice(this.binding.spinner.selectedItemPosition + 1)
            GeneralUtils.makeToast(context, "Saved!")
        }
    }

    private fun setBtnUseDefaultListener() {
        this.binding.useDefaultButton.setOnClickListener {
            ConstantUtils.smartTrendDetectorMargin = 5
            BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.SMART_TREND_DETECTOR, ConstantUtils.smartTrendDetectorMargin.toString())
            this.binding.spinner.setSelection(0)
            GeneralUtils.makeToast(context, "Default value set!")
        }
    }

    private fun setImgBtnSmartTrendDectectorListener() {
        this.binding.smartTrendDetectorImageButton.setOnClickListener {
            GeneralUtils.createAlertDialog(context, "Smart Trend Detector", """
         """.trimIndent(), false).show()
        }
    }

    private fun saveUserSmartTrendDectectorMarginPrice(smartTrendDetectorMargin: Int) {
        BaseSharedPreferencesService.save(requireContext(), BaseSharedPreferencesService.SMART_TREND_DETECTOR, smartTrendDetectorMargin.toString())
    }
}