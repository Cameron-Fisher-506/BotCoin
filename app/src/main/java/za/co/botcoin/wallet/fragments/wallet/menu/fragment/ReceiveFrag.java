package za.co.botcoin.wallet.fragments.wallet.menu.fragment;

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
import za.co.botcoin.MainActivity;
import za.co.botcoin.R;
import za.co.botcoin.menu.fragments.LunoApiFrag;
import za.co.botcoin.utils.ClipBoardUtils;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.FragmentUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.StringUtils;
import za.co.botcoin.utils.WSCallUtilsCallBack;
import za.co.botcoin.utils.WSCallsUtils;

import org.json.JSONObject;

public class ReceiveFrag extends Fragment implements WSCallUtilsCallBack
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
        View view = inflater.inflate(R.layout.frag_receive, container, false);

        initUI(view);

        if(GeneralUtils.isApiKeySet(getContext()))
        {
            getBotCoinAccountDetails();
        }else
        {
            GeneralUtils.createAlertDialog(getActivity(),"Luno API Credentials","Please set your Luno API credentials in order to use BotCoin!", false).show();

            LunoApiFrag lunoApiFrag = new LunoApiFrag();
            FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), lunoApiFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Luno API",true, false, true, null);
        }

        return view;
    }

    private void getBotCoinAccountDetails()
    {
        WSCallsUtils.get(this, REQ_CODE_FUNDING_ADDRESS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_FUNDING_ADDRESS + "?asset=" + this.asset, GeneralUtils.getAuth(ConstantUtils.KEY_ID, ConstantUtils.SECRET_KEY));
    }

    private void initUI(View view)
    {
        this.asset = getArguments().getString("asset");
        this.edTxtAddress =  view.findViewById(R.id.edTxtAddress);
        this.btnCopy =  view.findViewById(R.id.btnCopy);
        this.imgQRAddress = view.findViewById(R.id.imgQRAddress);

        addBtnCopyListener(view);
    }

    private void addBtnCopyListener(View view)
    {
        this.btnCopy = (Button) view.findViewById(R.id.btnCopy);
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
        if(response != null)
        {
            if (reqCode == REQ_CODE_FUNDING_ADDRESS) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject != null && jsonObject.has("error")) {
                        GeneralUtils.createAlertDialog(getActivity().getApplicationContext(), "Oops!", jsonObject.getString("error"), false);
                    } else {
                        this.address = jsonObject.getString("address");
                        this.edTxtAddress.setText(this.address);

                        this.qrCode = jsonObject.getString("qr_code_uri");
                        this.imgQRAddress.setImageBitmap(GeneralUtils.createQRCode(this.qrCode, this.imgQRAddress.getWidth(), this.imgQRAddress.getHeight()));
                    }

                } catch (Exception e) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: DonateFrag - taskCompleted"
                            + "\nRequest Code: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
        }else
        {

        }
    }
}
