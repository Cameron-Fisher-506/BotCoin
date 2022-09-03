package za.co.botcoin.view.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.AutoTradeFragmentBinding
import za.co.botcoin.services.FiboService
import za.co.botcoin.utils.*
import za.co.botcoin.utils.services.alertDialogService.AlertDialogService
import za.co.botcoin.utils.services.SharedPreferencesService

class AutoTradeFragment : AutoTradeBaseFragment(R.layout.auto_trade_fragment) {
    private lateinit var binding: AutoTradeFragmentBinding
    private val autoTradeViewModel by viewModels<AutoTradeViewModel>(factoryProducer = { autoTradeActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = AutoTradeFragmentBinding.bind(view)

        autoTradeViewModel.displayAutoTradeAlertDialog()

        if (GeneralUtils.isApiKeySet(context)) {
            setSwitchAutoTrade()
        } else {
            stopBotService()
            this.binding.autoTradeSwitch.isChecked = false
            GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false).show()

            val action = AutoTradeFragmentDirections.actionAutoTradeFragmentToLunoApiFragment()
            Navigation.findNavController(view).navigate(action)
        }

        this.binding.autoTradeSwitch.setOnCheckedChangeListener { _, isChecked ->
            context?.let { SharedPreferencesService.save(it, SharedPreferencesService.AUTO_TRADE_PREF, isChecked.toString()) }
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
            activity?.startForegroundService(Intent(activity, FiboService::class.java))
        } else {
            activity?.startService(Intent(activity, FiboService::class.java))
        }
    }

    private fun stopBotService() {
        activity?.stopService(Intent(activity, FiboService::class.java))
    }

    private fun setSwitchAutoTrade() {
        val isAutoTrade = context?.let { SharedPreferencesService[it, SharedPreferencesService.AUTO_TRADE_PREF] }
        this.binding.autoTradeSwitch.isChecked = isAutoTrade != null
    }
}