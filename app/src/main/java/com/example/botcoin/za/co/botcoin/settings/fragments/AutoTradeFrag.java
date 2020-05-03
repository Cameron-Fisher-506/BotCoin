package com.example.botcoin.za.co.botcoin.settings.fragments;

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

import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.utils.ConstantUtils;
import com.example.botcoin.za.co.botcoin.utils.GeneralUtils;
import com.example.botcoin.za.co.botcoin.utils.SharedPreferencesUtils;
import com.example.botcoin.za.co.botcoin.utils.StringUtils;

import org.json.JSONObject;

public class AutoTradeFrag extends Fragment {

    private Switch switchAutoTrade;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_auto_trade, container, false);

        this.switchAutoTrade = (Switch) view.findViewById(R.id.switchAutoTrade);
        setSwitchAutoTrade();

        this.switchAutoTrade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try
                {
                    JSONObject jsonObjectAutoTade = new JSONObject();
                    jsonObjectAutoTade.put("isAutoTrade", isChecked);
                    SharedPreferencesUtils.save(getContext(), SharedPreferencesUtils.AUTO_TRADE_PREF, jsonObjectAutoTade);
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
