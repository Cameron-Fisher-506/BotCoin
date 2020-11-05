package za.co.botcoin.navigation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import za.co.botcoin.R;
import za.co.botcoin.objs.Trade;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.viewpageradapters.TradeAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TradeFrag extends Fragment {

    public static final String TITLE = "Trading Bot";

    private TabLayout tabBotMenu;
    private ViewPager2 viewPager2;
    private List<Trade> trades;
    private TradeAdapter tradeAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_trade_option, container, false);


        this.tabBotMenu = view.findViewById(R.id.tabBotMenu);
        this.viewPager2 = view.findViewById(R.id.botPager);

        this.trades = new ArrayList<>();

        this.trades.add(new Trade(Trade.BUY_TYPE, "0", ConstantUtils.SUPPORT_PRICE));
        this.trades.add(new Trade(Trade.SELL_TYPE, "0", ConstantUtils.RESISTANCE_PRICE));


        this.tradeAdapter = new TradeAdapter(getContext(), this.trades);
        this.viewPager2.setAdapter(this.tradeAdapter);

        addTabBotMenuListener();

        return view;
    }


    private void addTabBotMenuListener()
    {
        this.tabBotMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition())
                {
                    case 0:
                        //BUY
                        viewPager2.setCurrentItem(0);

                        break;
                    case 1:
                        //SELL
                        viewPager2.setCurrentItem(1);

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
