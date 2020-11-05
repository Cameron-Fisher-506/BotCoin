package za.co.botcoin.settings.fragments;

import android.content.Intent;
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
        getActivity().startService(new Intent(getActivity(), BotService.class));
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
