package za.co.botcoin.policies;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONObject;

import za.co.botcoin.MainActivity;
import za.co.botcoin.R;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.SharedPreferencesUtils;


public class PrivacyPolicyFrag extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_privacy_policy, container, false);

        String privacyPolicy = "<p style=\"text-align:left; margin:4%\">\n" +
                "        Your privacy and legal rights are important to us. It is BotCoin's policy to respect your privacy regarding any information we may collect from you.<br><br>\n" +
                "\n" +
                "        We only ask for personal information that is required for us to provide a service to you. We collect it by fair and lawful means, with your knowledge and consent. We also let you know why we're collecting it and how it will be used.<br><br>\n" +
                "\n" +
                "        We only retain the collected information for as long as necessary to provide you with your requested service.\n" +
                "        The data we store, we will protect within commercially acceptable means to prevent loss and theft, as well as unauthorized access, disclosure, copying, use or modification.<br><br>\n" +
                "\n" +
                "        We don't share any personally identifying information publicly or with third-parties, except when required to by law.<br><br>\n" +
                "\n" +
                "        You are free to refuse our request for your personal information, with the understanding that we may be unable to provide you with some of your desired services.<br><br>\n" +
                "\n" +
                "        Your continued use of our app will be regarded as acceptance of our practices around privacy and personal information. If you have any questions about how we handle user data and personal information, feel free to contact us.<br><br>\n" +
                "\n" +
                "        <b><u>Personal and Sensitive Information Disclosure</u></b><br>\n" +
                "        To make use of the BotCoin app please note that we require and store the following information:<br>\n" +
                "        1. Your Luno API credentials.<br><br>\n" +
                "\n" +
                "        This policy is effective as of 1 November 2020.<br><br>\n" +
                "\n" +
                "        By accepting these above terms for personal and sensitivity information disclosure as to how your data will be used you give us permission to save your data for the terms as described above.\n" +
                "        \n" +
                "    </p>";
        TextView txtPrivacyPolicy = (TextView) view.findViewById(R.id.txtPrivacyPolicy);
        txtPrivacyPolicy.setMovementMethod(new ScrollingMovementMethod());
        txtPrivacyPolicy.setText(Html.fromHtml(privacyPolicy));

        Button btnExit = (Button)view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();
                System.exit(0);
            }
        });

        Button btnAccept = (Button) view.findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("isAccepted", true);

                    SharedPreferencesUtils.save(getContext(), SharedPreferencesUtils.PRIVACY_POLICY_ACCEPTANCE, jsonObject);

                    getContext().startActivity(new Intent(getActivity(), MainActivity.class));

                }catch(Exception e)
                {
                    Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nClass: PrivacyPolicyFrag" +
                            "\nMethod: onCreateView" +
                            "\nError: " + e.getMessage() +
                            "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
        });

        return view;
    }
}
