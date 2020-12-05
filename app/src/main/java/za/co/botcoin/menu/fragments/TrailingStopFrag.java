package za.co.botcoin.menu.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONObject;
import za.co.botcoin.R;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.SharedPreferencesUtils;

public class TrailingStopFrag extends Fragment
{

    private ImageButton imgBtnTrailingStop;
    private Button btnSave;
    private Button btnUseDefault;
    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_set_pullout_price, container, false);

        wireUI(view);
        setBtnSaveListener();
        setImgBtnTrailingStopListener();
        setBtnUseDefaultListener();

        return view;
    }


    private void wireUI(View view)
    {
        try
        {
            JSONObject jsonObject = SharedPreferencesUtils.get(getContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_USER);

            Integer itemPosition = null;
            if(jsonObject != null && jsonObject.has("itemPosition"))
            {
                itemPosition = jsonObject.getInt("itemPosition");
            }

            this.spinner = (Spinner) view.findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.trailing_stop_items, android.R.layout.simple_spinner_item);
            this.spinner.setAdapter(adapter);

            if(itemPosition != null)
            {
                this.spinner.setSelection(itemPosition);
            }

            this.imgBtnTrailingStop = (ImageButton) view.findViewById(R.id.imgBtnTrailingStop);
            this.btnSave = (Button) view.findViewById(R.id.btnSave);
            this.btnUseDefault = (Button) view.findViewById(R.id.btnUseDefault);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setBtnSaveListener()
    {
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ConstantUtils.trailingStop = Integer.parseInt(spinner.getSelectedItem().toString().replace("%",""));
                saveUserPullOutBidPrice(spinner.getSelectedItemPosition());
                GeneralUtils.makeToast(getContext(), "Saved!");
            }
        });
    }

    private void setBtnUseDefaultListener()
    {
        this.btnUseDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    if(SharedPreferencesUtils.get(getContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT) != null)
                    {
                        JSONObject jsonObject = SharedPreferencesUtils.get(getContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT);
                        if(jsonObject != null && jsonObject.has(SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT))
                        {
                            ConstantUtils.trailingStop = jsonObject.getInt(SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT);
                            GeneralUtils.makeToast(getContext(), "Default value set!");
                        }else
                        {
                            GeneralUtils.makeToast(getContext(), "Default value not found!");
                        }
                    }else
                    {
                        GeneralUtils.makeToast(getContext(), "Default value not found!");
                    }
                }catch (Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: MainActivity - saveDefaultPullOutBidPrice"
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
        });
    }

    private void setImgBtnTrailingStopListener()
    {
        this.imgBtnTrailingStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.createAlertDialog(getContext(), "Trailing Stop", "BotCoin uses the trailing stop percentage, to pullout of a trade if" +
                        " the market is in a downtrend.\n\n" +
                        "E.g.\n" +
                        "Trailing stop: 10%\n" +
                        "Current price: 100\n" +
                        "Sell order: 90\n\n" +
                        "In the above scenario BotCoin will create a sell order if the price drops 10% below than last highest resistance price."
                        , false).show();
            }
        });
    }

    private void saveUserPullOutBidPrice(int itemPosition)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(SharedPreferencesUtils.PULLOUT_BID_PRICE_USER, ConstantUtils.trailingStop);
            jsonObject.put("itemPosition", itemPosition);
            SharedPreferencesUtils.save(getContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_USER,jsonObject);

        }catch (Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - saveDefaultPullOutBidPrice"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

    }
}
