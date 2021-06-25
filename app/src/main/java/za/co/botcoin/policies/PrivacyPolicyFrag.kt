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
import za.co.botcoin.databinding.PrivacyPolicyFragmentBinding
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.SharedPreferencesUtils
import kotlin.system.exitProcess

class PrivacyPolicyFrag : Fragment(R.layout.privacy_policy_fragment) {
    private lateinit var binding: PrivacyPolicyFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = PrivacyPolicyFragmentBinding.bind(view)

        val privacyPolicy = """<p style="text-align:left; margin:4%">
        Your privacy and legal rights are important to us. It is BotCoin's policy to respect your privacy regarding any information we may collect from you.<br><br>

        We only ask for personal information that is required for us to provide a service to you. We collect it by fair and lawful means, with your knowledge and consent. We also let you know why we're collecting it and how it will be used.<br><br>

        We only retain the collected information for as long as necessary to provide you with your requested service.
        The data we store, we will protect within commercially acceptable means to prevent loss and theft, as well as unauthorized access, disclosure, copying, use or modification.<br><br>

        We don't share any personally identifying information publicly or with third-parties, except when required to by law.<br><br>

        You are free to refuse our request for your personal information, with the understanding that we may be unable to provide you with some of your desired services.<br><br>

        Your continued use of our app will be regarded as acceptance of our practices around privacy and personal information. If you have any questions about how we handle user data and personal information, feel free to contact us.<br><br>

        <b><u>Personal and Sensitive Information Disclosure</u></b><br>
        To make use of the BotCoin app please note that we require and store the following information:<br>
        1. Your Luno API credentials.<br><br>

        This policy is effective as of 1 November 2020.<br><br>

        By accepting these above terms for personal and sensitivity information disclosure as to how your data will be used you give us permission to save your data for the terms as described above.
        
    </p>"""

        this.binding.txtPrivacyPolicy.movementMethod = ScrollingMovementMethod()
        this.binding.txtPrivacyPolicy.text = Html.fromHtml(privacyPolicy)

        this.binding.btnExit.setOnClickListener {
            activity!!.finishAffinity()
            exitProcess(0)
        }

        this.binding.btnAccept.setOnClickListener {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("isAccepted", true)
                SharedPreferencesUtils.save(context, SharedPreferencesUtils.PRIVACY_POLICY_ACCEPTANCE, jsonObject)
                context!!.startActivity(Intent(activity, MainActivity::class.java))
            } catch (e: Exception) {
                Log.d(ConstantUtils.BOTCOIN_TAG, "Class: PrivacyPolicyFrag" +
                        "Method: onCreateView" +
                        "Error: ${e.message}" +
                        "CreatedTime: ${GeneralUtils.currentDateTime}")
            }
        }
    }
}