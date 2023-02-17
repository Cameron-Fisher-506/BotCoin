package za.co.botcoin.view.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.AutoTradeFragmentBinding
import za.co.botcoin.services.FiboService
import za.co.botcoin.utils.*

class AutoTradeSwitchFragment : AutoTradeBaseFragment(R.layout.auto_trade_fragment) {
    private lateinit var binding: AutoTradeFragmentBinding
    private val autoTradeSwitchViewModel by viewModels<AutoTradeSwitchViewModel>(factoryProducer = { autoTradeActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = AutoTradeFragmentBinding.bind(view)

        autoTradeSwitchViewModel.displayAutoTradeDisclaimerAlertDialog()

        if (GeneralUtils.isApiKeySet(context)) {
            setSwitchAutoTrade()
        } else {
            stopBotService()
            this.binding.autoTradeSwitch.isChecked = false
            autoTradeSwitchViewModel.displayLunoApiCredentialsAlertDialog()

            val action = AutoTradeSwitchFragmentDirections.actionAutoTradeSwitchFragmentToLunoApiFragment()
            Navigation.findNavController(view).navigate(action)
        }
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        this.binding.autoTradeSwitch.setOnCheckedChangeListener { _, isChecked ->
            autoTradeSwitchViewModel.saveAutoTradePref(isChecked)
            if (isChecked) {
                startBotService()
            } else {
                stopBotService()
            }
        }
        binding.autoTradeInformationView.setOnClickListener {
            autoTradeSwitchViewModel.displayAutoTradeInstructionsAlertDialog()
        }
    }

    private fun startBotService() {
        if (Build.VERSION.SDK_INT >= 26) {
            autoTradeActivity.startForegroundService(Intent(activity, FiboService::class.java))
        } else {
            autoTradeActivity.startService(Intent(activity, FiboService::class.java))
        }
    }

    private fun stopBotService() {
        autoTradeActivity.stopService(Intent(activity, FiboService::class.java))
    }

    private fun setSwitchAutoTrade() {
        val isAutoTrade = autoTradeSwitchViewModel.getAutoTradePref()
        this.binding.autoTradeSwitch.isChecked = isAutoTrade != null
    }
}