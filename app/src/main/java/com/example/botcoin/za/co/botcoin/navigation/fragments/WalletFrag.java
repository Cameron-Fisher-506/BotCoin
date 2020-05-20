package com.example.botcoin.za.co.botcoin.navigation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.utils.ConstantUtils;
import com.example.botcoin.za.co.botcoin.utils.GeneralUtils;
import com.example.botcoin.za.co.botcoin.utils.StringUtils;
import com.example.botcoin.za.co.botcoin.utils.WSCallUtilsCallBack;
import com.example.botcoin.za.co.botcoin.utils.WSCallsUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class WalletFrag extends Fragment implements WSCallUtilsCallBack {

    public final int BALANCE_REQ_CODE = 101;

    public static final int FRAG_NUM = 2;
    public static final String TITLE = "Wallet";

    private TextView txtZar;
    private TextView txtXrp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet, container, false);


        this.txtXrp = view.findViewById(R.id.txtXrp);
        this.txtZar = view.findViewById(R.id.txtZar);

        //Get ZAR and XRP balance
        WSCallsUtils.get(this, BALANCE_REQ_CODE,StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_BALANCE);

        return view;
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {

        if(reqCode == BALANCE_REQ_CODE)
        {
            try
            {
                if(GeneralUtils.isApiCredentialsSaved())
                {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null && jsonObject.has("balance"))
                    {
                        try
                        {
                            JSONArray jsonArrayBalance = jsonObject.getJSONArray("balance");
                            if(jsonArrayBalance != null && jsonArrayBalance.length() > 0)
                            {
                                for(int i = 0; i < jsonArrayBalance.length(); i++)
                                {
                                    JSONObject jsonObjectBalance = jsonArrayBalance.getJSONObject(i);

                                    String currency = jsonObjectBalance.getString("asset");
                                    String balance = jsonObjectBalance.getString("balance");
                                    String reserved = jsonObjectBalance.getString("reserved");

                                    if(currency.equals(ConstantUtils.XRP))
                                    {
                                        this.txtXrp.append(balance);

                                    }else if(currency.equals(ConstantUtils.ZAR))
                                    {
                                        this.txtZar.append(balance);
                                    }

                                }
                            }
                        }catch(Exception e)
                        {
                            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                                    + "\nMethod: MainActivity - onCreate"
                                    + "\nURL: " + StringUtils.GLOBAL_LUNO_URL + "/api/1/balance"
                                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                        }


                    }else
                    {
                        Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: No Response"
                                + "\nMethod: MainActivity - onCreate"
                                + "\nURL: " + StringUtils.GLOBAL_LUNO_URL + "/api/1/balance"
                                + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

                        GeneralUtils.createAlertDialog(getActivity(),"Luno API Credentials (Wallet)","Please ensure that the API ID and Secret Key that you have saved are correct!", false).show();
                    }
                }else
                {
                    GeneralUtils.createAlertDialog(getActivity(),"Luno API Credentials (Wallet)","Please set your Luno API credentials in order to use BotCoin!", false).show();
                }

            }catch(Exception e)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                        + "\nMethod: MainActivity - onCreate"
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }
        }

    }
}
