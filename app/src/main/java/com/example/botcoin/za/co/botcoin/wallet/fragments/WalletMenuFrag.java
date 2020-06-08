package com.example.botcoin.za.co.botcoin.wallet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import com.example.botcoin.MainActivity;
import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.utils.FragmentUtils;
import com.example.botcoin.za.co.botcoin.wallet.fragments.wallet.menu.fragment.ReceiveFrag;
import com.example.botcoin.za.co.botcoin.wallet.fragments.wallet.menu.fragment.SendFrag;

public class WalletMenuFrag extends Fragment
{
    private LinearLayoutCompat receiveOption;
    private LinearLayoutCompat sendOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet_menu, container, false);

        addReceiveListener(view);
        addSendListener(view);

        return view;
    }

    private void addSendListener(View view)
    {
        this.sendOption = view.findViewById(R.id.linearLayoutSendOption);
        this.sendOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("asset", getArguments().getString("asset"));

                SendFrag sendFrag = new SendFrag();
                sendFrag.setArguments(bundle);
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), sendFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Wallet Menu",true, false, true, null);
            }
        });
    }

    private void addReceiveListener(View view)
    {
        this.receiveOption = view.findViewById(R.id.linearLayoutReceiveOption);
        this.receiveOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("asset", getArguments().getString("asset"));

                ReceiveFrag receiveFrag = new ReceiveFrag();
                receiveFrag.setArguments(bundle);
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), receiveFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Wallet Menu",true, false, true, null);
            }
        });
    }
}
