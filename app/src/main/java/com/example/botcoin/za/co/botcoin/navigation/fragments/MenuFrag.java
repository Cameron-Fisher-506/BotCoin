package com.example.botcoin.za.co.botcoin.navigation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.botcoin.MainActivity;
import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.menu.fragments.DonateMenuFrag;
import com.example.botcoin.za.co.botcoin.menu.fragments.LunoApiFrag;
import com.example.botcoin.za.co.botcoin.menu.fragments.SupportResistanceFrag;
import com.example.botcoin.za.co.botcoin.utils.FragmentUtils;


public class MenuFrag extends Fragment {

    public static final String TITLE = "Menu";

    private LinearLayoutCompat lunoApiOption;
    private LinearLayoutCompat supportResistanceOption;
    private LinearLayoutCompat donateOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu, container, false);


        addLunoApiOptionListener(view.findViewById(R.id.linearLayoutLunoApiOption));
        addSupportResistanceListener(view.findViewById(R.id.linearLayoutSupportResistanceOption));
        addDonateListener(view.findViewById(R.id.linearLayoutDonateOption));

        return view;
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
