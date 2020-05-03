package com.example.botcoin.za.co.botcoin.navigation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.botcoin.MainActivity;
import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.menu.fragments.SupportResistanceFrag;
import com.example.botcoin.za.co.botcoin.objs.Trade;
import com.example.botcoin.za.co.botcoin.utils.ConstantUtils;
import com.example.botcoin.za.co.botcoin.utils.FragmentUtils;
import com.example.botcoin.za.co.botcoin.utils.GeneralUtils;
import com.example.botcoin.za.co.botcoin.viewpageradapters.BotAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BotFrag extends Fragment {

    public static final String TITLE = "Trading Bot";

    private TabLayout tabBotMenu;
    private ViewPager2 viewPager2;
    private List<Trade> trades;
    private BotAdapter botAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bot, container, false);


        this.tabBotMenu = view.findViewById(R.id.tabBotMenu);
        this.viewPager2 = view.findViewById(R.id.botPager);

        this.trades = new ArrayList<>();

        if(ConstantUtils.SUPPORT_PRICE != null && ConstantUtils.RESISTANCE_PRICE != null)
        {
            this.trades.add(new Trade(Trade.BUY_TYPE, "0", ConstantUtils.SUPPORT_PRICE));
            this.trades.add(new Trade(Trade.SELL_TYPE, "0", ConstantUtils.RESISTANCE_PRICE));
        }else
        {
            SupportResistanceFrag supportResistanceFrag = new SupportResistanceFrag();
            FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), supportResistanceFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Support/Resistance",true, false, true, null);

            ((MainActivity)getActivity()).setNavIcons(false,false,false,true);
            GeneralUtils.createAlertDialog(getContext(), "Support/Resistance", "Please set a Support and Resistance price before running your Trading Bot!", false).show();
        }

        this.botAdapter = new BotAdapter(getContext(), this.trades);
        this.viewPager2.setAdapter(this.botAdapter);

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
