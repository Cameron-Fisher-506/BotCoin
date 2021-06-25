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

public class SupportPriceCounterFrag extends Fragment
{

    private ImageButton imgBtnSupportPriceCounter;
    private Button btnSave;
    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_support_price_counter, container, false);

        wireUI(view);
        setBtnSaveListener();
        setImgBtnSupportPriceCounterListener();

        return view;
    }


    private void wireUI(View view)
    {
        try
        {
            JSONObject jsonObject = SharedPreferencesUtils.get(getContext(), SharedPreferencesUtils.SUPPORT_PRICE_COUNTER);

            Integer itemPosition = null;
            if(jsonObject != null && jsonObject.has("itemPosition"))
            {
                itemPosition = jsonObject.getInt("itemPosition");
            }

            this.spinner = (Spinner) view.findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.support_price_counter_items, android.R.layout.simple_spinner_item);
            this.spinner.setAdapter(adapter);

            if(itemPosition != null)
            {
                this.spinner.setSelection(itemPosition);
            }

            this.imgBtnSupportPriceCounter = (ImageButton) view.findViewById(R.id.imgBtnSupportPriceCounter);
            this.btnSave = (Button) view.findViewById(R.id.btnSave);
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
                ConstantUtils.supportPriceCounter = Integer.parseInt(spinner.getSelectedItem().toString());
                saveUserSupportPriceCounter(spinner.getSelectedItemPosition());
                GeneralUtils.makeToast(getContext(), "Saved!");
            }
        });
    }

    private void setImgBtnSupportPriceCounterListener()
    {
        this.imgBtnSupportPriceCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.createAlertDialog(getContext(), "Support Price Counter", "BotCoin uses the Support Price Counter, to buy at solid support price\n\n" +
                                "E.g.\n" +
                                "Support Price Counter: 5\n" +
                                "BotCoin keeps track of the number of hits each price gets. The lowest price with the highest number of hits > 5 will be set as the support price."
                        , false).show();
            }
        });
    }

    private void saveUserSupportPriceCounter(int itemPosition)
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(SharedPreferencesUtils.SUPPORT_PRICE_COUNTER, ConstantUtils.supportPriceCounter);
            jsonObject.put("itemPosition", itemPosition);
            SharedPreferencesUtils.save(getContext(), SharedPreferencesUtils.SUPPORT_PRICE_COUNTER,jsonObject);

        }catch (Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - saveUserSupportPriceCounter"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

    }
}
