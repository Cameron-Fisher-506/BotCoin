package za.co.botcoin.navigation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import za.co.botcoin.MainActivity;
import za.co.botcoin.R;
import za.co.botcoin.menu.fragments.DonateMenuFrag;
import za.co.botcoin.menu.fragments.LogcatFrag;
import za.co.botcoin.menu.fragments.LunoApiFrag;
import za.co.botcoin.menu.fragments.ResistancePriceCounterFrag;
import za.co.botcoin.menu.fragments.SupportPriceCounterFrag;
import za.co.botcoin.menu.fragments.TrailingStopFrag;
import za.co.botcoin.menu.fragments.SupportResistanceFrag;
import za.co.botcoin.utils.FragmentUtils;

public class MenuFrag extends Fragment {

    public static final String TITLE = "Menu";

    private LinearLayoutCompat lunoApiOption;
    private LinearLayoutCompat supportResistanceOption;
    private LinearLayoutCompat donateOption;
    private LinearLayoutCompat setPullOutPriceOption;
    private LinearLayoutCompat logcatOption;
    private LinearLayoutCompat setSupportPriceCounter;
    private LinearLayoutCompat setResistancePriceCounter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu, container, false);

        addLunoApiOptionListener(view.findViewById(R.id.linearLayoutLunoApiOption));
        addSupportResistanceListener(view.findViewById(R.id.linearLayoutSupportResistanceOption));
        addDonateListener(view.findViewById(R.id.linearLayoutDonateOption));
        addPullOutPriceOptionListener(view.findViewById(R.id.linearLayoutSetPullOutPrice));
        addLogcatOptionListener(view.findViewById(R.id.linearLayoutLogcat));
        addSetSupportPriceCounterOptionListener(view.findViewById(R.id.linearLayoutSetSupportPriceCounter));
        addSetResistancePriceCounterOptionListener(view.findViewById(R.id.linearLayoutSetResistancePriceCounter));

        return view;
    }

    private void addSetResistancePriceCounterOptionListener(View view)
    {
        this.setResistancePriceCounter = (LinearLayoutCompat) view;
        this.setResistancePriceCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ResistancePriceCounterFrag resistancePriceCounterFrag = new ResistancePriceCounterFrag();
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), resistancePriceCounterFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Set Resistance Price Counter",true, false, true, null);
            }
        });
    }

    private void addSetSupportPriceCounterOptionListener(View view)
    {
        this.setSupportPriceCounter = (LinearLayoutCompat) view;
        this.setSupportPriceCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SupportPriceCounterFrag supportPriceCounterFrag = new SupportPriceCounterFrag();
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), supportPriceCounterFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Set Support Price Counter",true, false, true, null);
            }
        });
    }

    private void addLogcatOptionListener(View view)
    {
        this.logcatOption = (LinearLayoutCompat) view;
        this.logcatOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LogcatFrag logcatFrag = new LogcatFrag();
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), logcatFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Logcat",true, false, true, null);
            }
        });
    }

    private void addPullOutPriceOptionListener(View view)
    {
        this.setPullOutPriceOption = (LinearLayoutCompat) view;
        this.setPullOutPriceOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TrailingStopFrag setPullOutPriceFrag = new TrailingStopFrag();
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), setPullOutPriceFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Set Pull-out Price",true, false, true, null);
            }
        });
    }

    private void addLunoApiOptionListener(View view)
    {
        this.lunoApiOption = (LinearLayoutCompat) view;
        this.lunoApiOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LunoApiFrag lunoApiFrag = new LunoApiFrag();
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), lunoApiFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Luno API",true, false, true, null);
            }
        });
    }

    private void addSupportResistanceListener(View view)
    {
        this.supportResistanceOption = (LinearLayoutCompat) view;
        this.supportResistanceOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportResistanceFrag supportResistanceFrag = new SupportResistanceFrag();
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), supportResistanceFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Support/Resistance",true, false, true, null);

            }
        });
    }

    private void addDonateListener(View view)
    {
        this.donateOption = (LinearLayoutCompat) view;
        this.donateOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonateMenuFrag donateFrag = new DonateMenuFrag();
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), donateFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Donate Menu", true, false,true, null);
            }
        });
    }

}
