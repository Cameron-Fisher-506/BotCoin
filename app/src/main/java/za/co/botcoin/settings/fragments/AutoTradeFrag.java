package za.co.botcoin.settings.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import za.co.botcoin.MainActivity;
import za.co.botcoin.R;
import za.co.botcoin.menu.fragments.LunoApiFrag;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.DialogUtils;
import za.co.botcoin.utils.FragmentUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.SharedPreferencesUtils;
import za.co.botcoin.services.BotService;

import org.json.JSONObject;

public class AutoTradeFrag extends Fragment {

    private Switch switchAutoTrade;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_auto_trade, container, false);

        wireUI(view);

        DialogUtils.createAlertDialog(getContext(), "Disclaimer",
                "Any client deciding to use BotCoin understands that:\n\n" +
                "1. Trading cryptocurrency involves substantial risk, and there is always the potential for loss.\n" +
                "2. Trading results on BotCoin may vary. BotCoin does not guarantee that you will always make a profit as cryptocurrency prices are volatile.\n" +
                "3. Past performance is not indicative of future results. A trader who has been successful for a substantial amount of time may not always be successful.\n" +
                "4. The decision of whether to use the service offered is that of the client alone.\n" +
                "5. BotCoin nor any of the developers, will be responsible for any loss.\n" +
                "6. N.B. BotCoin will not make the decision to automatically pull out of a trades if the price drops as this is a very risky decision to make. \n" +
                "The user is responsible for pulling out of a trade if the price, of the cryptocurrency he/she is trading, drops.\n" +
                "7. N.B. BotCoin consumes Luno's API. Therefore, Luno charges still apply when withdrawing money and sending money.\n" +
                "8. BotCoin provides it's services for FREE. Users have the option to donate cryptocurrency to BotCoin. Donations are non-refundable.\n\n" +

                "Your continued use of our app will be regarded as acceptance of the risk involved.\n\n" +
                "Disclosure\n\n" +
                "To make use of the BotCoin app please note that we require and store the following information:\n" +
                "1. Your Luno API credentials.\n\n" +
                "This policy is effective as of 25 November 2020.\n\n", false).show();

        if(GeneralUtils.isApiKeySet(getContext()))
        {
            setSwitchAutoTrade();
        }else
        {
            stopBotService();
            this.switchAutoTrade.setChecked(false);

            GeneralUtils.createAlertDialog(getActivity(),"Luno API Credentials","Please set your Luno API credentials in order to use BotCoin!", false).show();

            LunoApiFrag lunoApiFrag = new LunoApiFrag();
            FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), lunoApiFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Luno API",true, false, true, null);
        }

        this.switchAutoTrade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try
                {
                    JSONObject jsonObjectAutoTade = new JSONObject();
                    jsonObjectAutoTade.put("isAutoTrade", isChecked);
                    SharedPreferencesUtils.save(getContext(), SharedPreferencesUtils.AUTO_TRADE_PREF, jsonObjectAutoTade);

                    if(isChecked)
                    {
                        //start service
                        startBotService();
                    }else
                    {
                        //stop service
                        stopBotService();
                    }
                }catch(Exception e)
                {

                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: AutoTradeFrag - onCreateView"
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
        });

        return view;
    }

    private void wireUI(View view)
    {
        this.switchAutoTrade = view.findViewById(R.id.switchAutoTrade);
    }

    private void startBotService()
    {
        if (Build.VERSION.SDK_INT >= 26) {
            getActivity().startForegroundService(new Intent(getActivity(), BotService.class));
        }else
        {
            getActivity().startService(new Intent(getActivity(), BotService.class));
        }

    }

    private void stopBotService()
    {
        getActivity().stopService(new Intent(getActivity(), BotService.class));
    }


    private void setSwitchAutoTrade()
    {
        JSONObject jsonObjectAutoTrade =  SharedPreferencesUtils.get(getContext(), SharedPreferencesUtils.AUTO_TRADE_PREF);
        if(jsonObjectAutoTrade != null && jsonObjectAutoTrade.has("isAutoTrade"))
        {
            try
            {
                boolean isAutoTrade = jsonObjectAutoTrade.getBoolean("isAutoTrade");
                this.switchAutoTrade.setChecked(isAutoTrade);
            }catch(Exception e)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                        + "\nMethod: AutoTradeFrag - setSwitchAutoTrade"
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }

        }else
        {
            this.switchAutoTrade.setChecked(false);
        }
    }


}
