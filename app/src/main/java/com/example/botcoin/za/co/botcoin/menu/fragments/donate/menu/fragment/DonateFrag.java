package com.example.botcoin.za.co.botcoin.menu.fragments.donate.menu.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.utils.ClipBoardUtils;
import com.example.botcoin.za.co.botcoin.utils.ConstantUtils;
import com.example.botcoin.za.co.botcoin.utils.GeneralUtils;
import com.example.botcoin.za.co.botcoin.utils.StringUtils;
import com.example.botcoin.za.co.botcoin.utils.WSCallUtilsCallBack;
import com.example.botcoin.za.co.botcoin.utils.WSCallsUtils;

import org.json.JSONObject;

public class DonateFrag extends Fragment implements WSCallUtilsCallBack
{
    private final int REQ_CODE_FUNDING_ADDRESS = 101;

    private EditText edTxtAddress;
    private Button btnCopy;
    private ImageView imgQRAddress;

    private String address;
    private String qrCode;
    private String asset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_donate, container, false);

        initUI(view);

        getBotCoinAccountDetails();

        return view;
    }


    private void initUI(View view)
    {
        this.asset = getArguments().getString("asset");

        this.edTxtAddress = view.findViewById(R.id.edTxtAddress);
        this.btnCopy = view.findViewById(R.id.btnCopy);
        this.imgQRAddress = view.findViewById(R.id.imgQRAddress);

        addBtnCopyListener(view.findViewById(R.id.btnCopy));
    }

    private void getBotCoinAccountDetails()
    {
        WSCallsUtils.get(this, REQ_CODE_FUNDING_ADDRESS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_FUNDING_ADDRESS + "?asset=" + this.asset);
    }

    private void addBtnCopyListener(View view)
    {
        this.btnCopy = (Button) view;
        this.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipBoardUtils.copyToClipBoard(getActivity(), address);
            }
        });
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(reqCode == REQ_CODE_FUNDING_ADDRESS)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);

                if(jsonObject.has("error"))
                {
                    GeneralUtils.createAlertDialog(getActivity().getApplicationContext(), "Oops!", jsonObject.getString("error"), false);
                }else
                {
                    this.address = jsonObject.getString("address");
                    this.edTxtAddress.setText(this.address);

                    this.qrCode = jsonObject.getString("qr_code_uri");
                    this.imgQRAddress.setImageBitmap(GeneralUtils.createQRCode(this.qrCode, this.imgQRAddress.getWidth(), this.imgQRAddress.getHeight()));
                }

            }catch (Exception e)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                        + "\nMethod: DonateFrag - taskCompleted"
                        + "\nRequest Code: " + reqCode
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }
        }
    }
}
