package com.example.botcoin.za.co.botcoin.menu.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.utils.ConstantUtils;
import com.example.botcoin.za.co.botcoin.utils.GeneralUtils;
import com.example.botcoin.za.co.botcoin.utils.SharedPreferencesUtils;

import org.json.JSONObject;

public class LunoApiFrag extends Fragment
{

    public static final int FRAG_NUM = 4;
    public static final String TITLE = "Luno API";

    private EditText edTxtKeyID;
    private EditText edTxtSecretKey;
    private Button btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_luno_api, container, false);

        this.edTxtKeyID = (EditText) view.findViewById(R.id.edTxtKeyID);
        this.edTxtSecretKey = (EditText) view.findViewById(R.id.edTxtSecretKey);

        setBtnSaveListener(view.findViewById(R.id.btnSave));
        return view;
    }


    private void setBtnSaveListener(View view)
    {
        this.btnSave = (Button) view;
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyID = edTxtKeyID.getText().toString();
                String secretKey = edTxtSecretKey.getText().toString();

                if(!keyID.equals("") && !secretKey.equals(""))
                {
                    ConstantUtils.USER_KEY_ID = keyID;
                    ConstantUtils.USER_SECRET_KEY = secretKey;

                    try
                    {
                        JSONObject jsonObjectApiKey = new JSONObject();
                        jsonObjectApiKey.put("keyID", keyID);
                        jsonObjectApiKey.put("secretKey", secretKey);

                        SharedPreferencesUtils.save(getActivity(), SharedPreferencesUtils.LUNO_API_PREF, jsonObjectApiKey);
                        GeneralUtils.makeToast(getActivity(), "API Key Saved!");
                    }catch(Exception e)
                    {
                        Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                                + "\nMethod: LunoApiFrag - onCreateView"
                                + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                    }
                }else
                {
                    GeneralUtils.createAlertDialog(getActivity(), "Luno API Credentials (Luno API)", "Please set your Luno API credentials in order to use BotCoin!", false).show();

                }


            }
        });
    }
}
