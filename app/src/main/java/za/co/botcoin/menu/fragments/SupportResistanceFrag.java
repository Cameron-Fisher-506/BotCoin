package za.co.botcoin.menu.fragments;

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
import za.co.botcoin.R;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.SharedPreferencesUtils;
import org.json.JSONObject;

public class SupportResistanceFrag extends Fragment
{

    private EditText txtSupport;
    private EditText txtResistance;
    private Button btnApply;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_support_resistance, container, false);

        this.txtResistance = (EditText) view.findViewById(R.id.txtResistance);
        this.txtSupport = (EditText) view.findViewById(R.id.txtSupport);

        addApplyListener(view.findViewById(R.id.btnApply));

        return view;
    }

    private void addApplyListener(View view)
    {
        this.btnApply = (Button) view;
        this.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supportPrice = txtSupport.getText().toString();
                String resistancePrice = txtResistance.getText().toString();

                if(!supportPrice.equals("") && !resistancePrice.equals(""))
                {
                    try
                    {
                        JSONObject jsonObjectSupportResistance = new JSONObject();
                        jsonObjectSupportResistance.put("supportPrice", supportPrice);
                        jsonObjectSupportResistance.put("resistancePrice", resistancePrice);

                        SharedPreferencesUtils.save(getContext(), SharedPreferencesUtils.SUPPORT_RESISTANCE_PREF, jsonObjectSupportResistance);
                    }catch(Exception e)
                    {
                        Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                                + "\nMethod: SupportResistanceFrag - addApplyListener"
                                + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                    }
                    GeneralUtils.makeToast(getContext(), "Support and Resistance applied!");
                }else
                {
                    GeneralUtils.createAlertDialog(getContext(), "Support/Resistance", "Please enter a Support and Resistance price before applying the change!", false).show();
                }
            }
        });
    }
}
