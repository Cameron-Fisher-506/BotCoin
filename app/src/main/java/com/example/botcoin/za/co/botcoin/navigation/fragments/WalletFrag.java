package com.example.botcoin.za.co.botcoin.navigation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import com.example.botcoin.MainActivity;
import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.utils.ConstantUtils;
import com.example.botcoin.za.co.botcoin.utils.FragmentUtils;
import com.example.botcoin.za.co.botcoin.utils.GeneralUtils;
import com.example.botcoin.za.co.botcoin.utils.StringUtils;
import com.example.botcoin.za.co.botcoin.utils.WSCallUtilsCallBack;
import com.example.botcoin.za.co.botcoin.utils.WSCallsUtils;
import com.example.botcoin.za.co.botcoin.wallet.fragments.WalletMenuFrag;
import com.example.botcoin.za.co.botcoin.wallet.fragments.WithdrawFrag;
import org.json.JSONArray;
import org.json.JSONObject;

public class WalletFrag extends Fragment implements WSCallUtilsCallBack {

    public final int BALANCE_REQ_CODE = 101;

    private TextView txtZar;
    private TextView txtXrp;
    private LinearLayoutCompat zarOption;
    private LinearLayoutCompat xrpOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet, container, false);

        wireUI(view);

        addZarOptionListener(view);
        addXrpOptionListener(view);

        if(GeneralUtils.isApiKeySet(getContext()))
        {
            //Get ZAR and XRP balance
            WSCallsUtils.get(this, BALANCE_REQ_CODE,StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_BALANCE,GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));

        }else
        {
            GeneralUtils.createAlertDialog(getActivity(),"Luno API Credentials","Please set your Luno API credentials in order to use BotCoin!", false).show();
        }

        return view;
    }

    private void wireUI(View view)
    {
        this.txtXrp = view.findViewById(R.id.txtXrp);
        this.txtZar = view.findViewById(R.id.txtZar);
    }

    private void addZarOptionListener(View view)
    {
        this.zarOption = (LinearLayoutCompat) view.findViewById(R.id.linearLayoutZar);
        this.zarOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WithdrawFrag withdrawFrag = new WithdrawFrag();
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), withdrawFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Withdraw",true, false, true, null);
            }
        });
    }

    private void addXrpOptionListener(View view)
    {
        this.xrpOption = view.findViewById(R.id.linearLayoutXrp);
        this.xrpOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("asset", ConstantUtils.XRP);

                WalletMenuFrag walletMenuFrag = new WalletMenuFrag();
                walletMenuFrag.setArguments(bundle);
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), walletMenuFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Wallet Menu",true, false, true, null);
            }
        });
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(response != null)
        {
            if(reqCode == BALANCE_REQ_CODE)
            {
                try
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


                    }


                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: MainActivity - onCreate"
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
        }else
        {
            GeneralUtils.createAlertDialog(((MainActivity)getActivity()), "No Signal", "Please check your network connection!", false);
        }


    }
}
