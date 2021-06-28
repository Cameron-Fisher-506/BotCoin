package za.co.botcoin.settings.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import org.json.JSONObject
import za.co.botcoin.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.AutoTradeFragmentBinding
import za.co.botcoin.menu.fragments.LunoApiFrag
import za.co.botcoin.services.BotService
import za.co.botcoin.utils.*
import java.util.*

class AutoTradeFragment : Fragment(R.layout.auto_trade_fragment) {
    private lateinit var binding: AutoTradeFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = AutoTradeFragmentBinding.bind(view)

        DialogUtils.createAlertDialog(context, "Disclaimer",
                """
                    Any client deciding to use BotCoin understands that:
                    
                    1. Trading cryptocurrency involves substantial risk, and there is always the potential for loss.
                    2. Trading results on BotCoin may vary. BotCoin does not guarantee that you will always make a profit as cryptocurrency prices are volatile.
                    3. Past performance is not indicative of future results. A trader who has been successful for a substantial amount of time may not always be successful.
                    4. The decision of whether to use the service offered is that of the client alone.
                    5. BotCoin nor any of the developers, will be responsible for any loss.
                    6. N.B. BotCoin will not make the decision to automatically pull out of a trades if the price drops as this is a very risky decision to make. 
                    The user is responsible for pulling out of a trade if the price, of the cryptocurrency he/she is trading, drops.
                    7. N.B. BotCoin consumes Luno's API. Therefore, Luno charges still apply when withdrawing money and sending money.
                    8. BotCoin provides it's services for FREE. Users have the option to donate cryptocurrency to BotCoin. Donations are non-refundable.
                    
                    Your continued use of our app will be regarded as acceptance of the risk involved.
                    
                    Disclosure
                    
                    To make use of the BotCoin app please note that we require and store the following information:
                    1. Your Luno API credentials.
                    
                    This policy is effective as of 25 November 2020.
                    
                    
                    """.trimIndent(), false)?.show()

        if (GeneralUtils.isApiKeySet(context)) {
            setSwitchAutoTrade()
        } else {
            stopBotService()
            this.binding.switchAutoTrade.isChecked = false
            GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false)?.show()

            val action = AutoTradeFragmentDirections.actionAutoTradeFragmentToLunoApiFrag()
            Navigation.findNavController(view).navigate(action)
        }

        this.binding.switchAutoTrade.setOnCheckedChangeListener { _, isChecked ->
            try {
                val jsonObjectAutoTade = JSONObject()
                jsonObjectAutoTade.put("isAutoTrade", isChecked)
                context?.let { SharedPreferencesUtils.save(it, SharedPreferencesUtils.AUTO_TRADE_PREF, jsonObjectAutoTade) }
                if (isChecked) {
                    //start service
                    startBotService()
                } else {
                    //stop service
                    stopBotService()
                }
            } catch (e: Exception) {
                Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                        "Method: AutoTradeFrag - onCreateView " +
                        "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
            }
        }
    }

    private fun startBotService() {
        if (Build.VERSION.SDK_INT >= 26) {
            activity?.startForegroundService(Intent(activity, BotService::class.java))
        } else {
            activity?.startService(Intent(activity, BotService::class.java))
        }
    }

    private fun stopBotService() {
        activity?.stopService(Intent(activity, BotService::class.java))
    }

    private fun setSwitchAutoTrade() {
        val jsonObjectAutoTrade = context?.let { SharedPreferencesUtils.get(it, SharedPreferencesUtils.AUTO_TRADE_PREF) }
        if (jsonObjectAutoTrade != null && jsonObjectAutoTrade.has("isAutoTrade")) {
            try {
                val isAutoTrade = jsonObjectAutoTrade.getBoolean("isAutoTrade")
                this.binding.switchAutoTrade.isChecked = isAutoTrade
            } catch (e: Exception) {
                Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                        "Method: AutoTradeFrag - setSwitchAutoTrade " +
                        "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
            }
        } else {
            this.binding.switchAutoTrade.isChecked = false
        }
    }
}