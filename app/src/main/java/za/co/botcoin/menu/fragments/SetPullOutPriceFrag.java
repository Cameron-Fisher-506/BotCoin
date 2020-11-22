package za.co.botcoin.menu.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONObject;
import za.co.botcoin.R;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.SharedPreferencesUtils;

public class SetPullOutPriceFrag extends Fragment
{

    private ImageButton imgBtnPulloutBidPrice;
    private EditText edTxtPulloutBidPrice;

    private Button btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_set_pullout_price, container, false);

        wireUI(view);
        setBtnSaveListener();
        setImgBtnPulloutBidPriceListener();

        return view;
    }


    private void wireUI(View view)
    {
        this.imgBtnPulloutBidPrice = (ImageButton) view.findViewById(R.id.imgBtnPulloutBidPrice);

        this.edTxtPulloutBidPrice = (EditText) view.findViewById(R.id.edTxtPulloutBidPrice);
        this.edTxtPulloutBidPrice.setText(Double.toString(ConstantUtils.PULL_OUT_PRICE_DROP));

        this.btnSave = (Button) view.findViewById(R.id.btnSave);
    }

    private void setBtnSaveListener()
    {
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ConstantUtils.PULL_OUT_PRICE_DROP = Double.parseDouble(edTxtPulloutBidPrice.getText().toString());
                saveUserPullOutBidPrice();
                GeneralUtils.makeToast(getContext(), "Saved!");
            }
        });
    }

    private void setImgBtnPulloutBidPriceListener()
    {
        this.imgBtnPulloutBidPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.createAlertDialog(getContext(), "Pull-out Bid Price", "BotCoin uses the pull-out bid price, as a margin, to stop a buy order if" +
                        " the current price is higher than the last buy order price.\n\n" +
                        "E.g.\n" +
                        "Pull-out Bid Price: 0.5\n" +
                        "Current price: 4\n" +
                        "Last Bid Order Price: 3.5\n\n" +
                        "In the above scenario BotCoin will cancel the last bid order and place a new bid order at a new support price."
                        , false).show();
            }
        });
    }

    private void saveUserPullOutBidPrice()
    {
        try
        {
            if(SharedPreferencesUtils.get(getContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_USER) == null)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(SharedPreferencesUtils.PULLOUT_BID_PRICE_USER, ConstantUtils.PULL_OUT_PRICE_DROP);
                SharedPreferencesUtils.save(getContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_USER,jsonObject);
            }
        }catch (Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - saveDefaultPullOutBidPrice"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

    }
}
