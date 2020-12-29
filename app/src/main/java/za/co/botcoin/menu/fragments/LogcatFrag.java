package za.co.botcoin.menu.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import za.co.botcoin.R;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.GeneralUtils;

public class LogcatFrag extends Fragment
{

    private TextView txtLogcat = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_logcat, container, false);

        wireUI(view);
        displayLogcat(view);

        return view;
    }

    private void wireUI(View view)
    {
        this.txtLogcat = (TextView) view.findViewById(R.id.txtLogcat);
    }

    private void displayLogcat(View view)
    {
        if(ConstantUtils.resistancePrices != null && ConstantUtils.resistancePrices.size() > 0)
        {
            StringBuilder prices = new StringBuilder();
            for(int i = 0; i < ConstantUtils.resistancePrices.size(); i++)
            {
                prices.append("["+ConstantUtils.resistancePrices.get(i).getPrice()+", "+ConstantUtils.resistancePrices.get(i).getCounter()+"]\n");
            }

            this.txtLogcat.append("\n\nBotService - setResistancePrice"
                    + "\nResistancePrices: " + prices.toString()
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

        if(ConstantUtils.supportPrices != null && ConstantUtils.supportPrices.size() > 0)
        {
            StringBuilder prices = new StringBuilder();
            for(int i = 0; i < ConstantUtils.supportPrices.size(); i++)
            {
                prices.append("["+ConstantUtils.supportPrices.get(i).getPrice()+", "+ConstantUtils.supportPrices.get(i).getCounter()+"]\n");
            }

            this.txtLogcat.append("\n\nBotService - setSupportPrice"
                    + "\nSupportPrices: " + prices.toString()
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }
    }

}
