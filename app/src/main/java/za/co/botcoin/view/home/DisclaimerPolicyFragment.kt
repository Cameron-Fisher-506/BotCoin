package za.co.botcoin.view.home

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.DisclaimerPolicyFragmentBinding
import za.co.botcoin.utils.SharedPrefsUtils
import kotlin.system.exitProcess

class DisclaimerPolicyFragment : Fragment(R.layout.disclaimer_policy_fragment) {
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

        this.binding.disclaimerPolicyTextView.movementMethod = ScrollingMovementMethod()
        this.binding.disclaimerPolicyTextView.text = Html.fromHtml(privacyPolicy)

        this.binding.exitButton.setOnClickListener {
            activity!!.finishAffinity()
            exitProcess(0)
        }

        this.binding.acceptButton.setOnClickListener {
            context?.let { it1 -> SharedPrefsUtils.save(it1, SharedPrefsUtils.DISCLAIMER_ACCEPTANCE, "true") }
            context?.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}