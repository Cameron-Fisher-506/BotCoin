package za.co.botcoin.policies

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.json.JSONObject
import za.co.botcoin.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.DisclaimerPolicyFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPreferencesUtils
import kotlin.system.exitProcess

class DisclaimerPolicyFrag : Fragment(R.layout.disclaimer_policy_fragment) {
    private lateinit var binding: DisclaimerPolicyFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = DisclaimerPolicyFragmentBinding.bind(view)

        val privacyPolicy = "<p style=\"text-align:left; margin-left:4%; margin-right:4%\">" +
                "        " +
                "        <h3 style=\"text-align:left; margin-left:4%; margin-right:4%\">Any client deciding to use BotCoin understands that:</h3>" +
                "            1. Trading cryptocurrency involves substantial risk, and there is always the potential for loss.<br>" +
                "            2. Trading results on BotCoin may vary. BotCoin does not guarantee that you will always make a profit as cryptocurrency prices are volatile.<br>" +
                "            3. Past performance is not indicative of future results. A trader who has been successful for a substantial amount of time may not always be successful.<br>" +
                "            4. The decision of whether to use the service offered is that of the client alone.<br>" +
                "            5. BotCoin nor any of the developers, will be responsible for any loss.<br>" +
                "            6. N.B. BotCoin will not make the decision to automatically pull out of a trades if the price drops as this is a very risky decision to make. <br>" +
                "                The user is responsible for pulling out of a trade if the price, of the cryptocurrency he/she is trading, drops.<br>" +
                "            7. N.B. BotCoin consumes Luno's API. Therefore, Luno charges still apply when withdrawing money and sending money.<br>" +
                "            8. BotCoin provides it's services for FREE. Users have the option to donate cryptocurrency to BotCoin. Donations are non-refundable.<br>" +
                "    </p>" +
                "    <p style=\"text-align:left; margin-left:4%; margin-right:4%\">" +
                "        Your continued use of our app will be regarded as acceptance of the risk involved.<br><br>" +
                "        <b><u>Disclosure</u></b><br>" +
                "        To make use of the BotCoin app please note that we require and store the following information:<br>" +
                "        1. Your Luno API credentials.<br><br>" +
                "" +
                "        This policy is effective as of 25 November 2020.<br><br>" +
                "    </p>"

        this.binding.txtDisclaimerPolicy.movementMethod = ScrollingMovementMethod()
        this.binding.txtDisclaimerPolicy.text = Html.fromHtml(privacyPolicy)

        this.binding.btnExit.setOnClickListener {
            activity!!.finishAffinity()
            exitProcess(0)
        }

        this.binding.btnAccept.setOnClickListener {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("isAccepted", true)
                SharedPreferencesUtils.save(context, SharedPreferencesUtils.DISCLAIMER_ACCEPTANCE, jsonObject)
                context!!.startActivity(Intent(activity, MainActivity::class.java))
            } catch (e: Exception) {
                Log.d(ConstantUtils.BOTCOIN_TAG, "Class: DisclaimerPolicyFrag " +
                        "Method: onCreateView " +
                        "Error: ${e.message} " +
                        "CreatedTime: ${GeneralUtils.currentDateTime}")
            }
        }
    }
}