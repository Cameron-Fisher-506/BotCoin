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
import za.co.botcoin.utils.services.sharePreferencesService.BaseSharedPreferencesService

class AutoTradeFragment : AutoTradeBaseFragment(R.layout.auto_trade_fragment) {
    private lateinit var binding: AutoTradeFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = AutoTradeFragmentBinding.bind(view)

        autoTradeViewModel.displayAutoTradeAlertDialog()

        if (GeneralUtils.isApiKeySet(context)) {
            setSwitchAutoTrade()
        } else {
            stopBotService()
            this.binding.autoTradeSwitch.isChecked = false
            autoTradeViewModel.displayLunoApiCredentialsAlertDialog()

            val action = AutoTradeFragmentDirections.actionAutoTradeFragmentToLunoApiFragment()
            Navigation.findNavController(view).navigate(action)
        }
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        this.binding.autoTradeSwitch.setOnCheckedChangeListener { _, isChecked ->
            autoTradeViewModel.saveAutoTradePref(isChecked)
            if (isChecked) {
                //start service
                startBotService()
            } else {
                //stop service
                stopBotService()
            }
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
        val isAutoTrade = autoTradeViewModel.getAutoTradePref()
        this.binding.autoTradeSwitch.isChecked = isAutoTrade != null
    }
}