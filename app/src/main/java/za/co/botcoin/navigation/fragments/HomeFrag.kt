package za.co.botcoin.navigation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import org.json.JSONObject
import za.co.botcoin.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.HomeFragmentBinding
import za.co.botcoin.utils.*
import java.util.*

class HomeFrag : Fragment(R.layout.home_fragment), WSCallUtilsCallBack {
    private lateinit var binding: HomeFragmentBinding

    private val TICKERS_REQ_CODE = 101
    private var timerTask: TimerTask? = null
    private var timer: Timer? = null

    companion object {
        const val TITLE = "Home"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = HomeFragmentBinding.bind(view)

        if (GeneralUtils.isApiKeySet(context)) {
            timerTask = object : TimerTask() {
                override fun run() {
                    tickers
                }
            }
            timer = Timer()
            timer!!.scheduleAtFixedRate(timerTask, 0, ConstantUtils.TICKER_RUN_TIME.toLong())
        } else {
            GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false).show()
        }
    }

    private val tickers: Unit
        private get() {
            WSCallsUtils.get(this, TICKERS_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_TICKERS, GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
        }

    override fun onDestroyView() {
        super.onDestroyView()
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
        }
    }

    override fun taskCompleted(response: String, reqCode: Int) {
        if (response != null) {
            if (reqCode == TICKERS_REQ_CODE) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("tickers")) {
                        val tickers = jsonObject.getJSONArray("tickers")
                        if (tickers != null && tickers.length() > 0) {
                            for (i in 0 until tickers.length()) {
                                val ticker = tickers.getJSONObject(i)
                                val pair = ticker.getString("pair")
                                if (pair == ConstantUtils.PAIR_XRPZAR) {
                                    val lastTrade = ticker.getString("last_trade")
                                    (activity as MainActivity?)!!.runOnUiThread {
                                        this.binding.txtXrpZar.text = R.string.XRPZAR.toString()
                                        this.binding.txtXrpZar.append(lastTrade)
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                            "Method: HomeFrag - onCreateView " +
                            "URL: ${StringUtils.GLOBAL_ENDPOINT_TICKERS} " +
                            "CreatedTime: ${GeneralUtils.getCurrentDateTime()}")
                }
            }
        } else {
            GeneralUtils.createAlertDialog(activity as MainActivity?, "No Signal", "Please check your network connection!", false)
        }
    }
}