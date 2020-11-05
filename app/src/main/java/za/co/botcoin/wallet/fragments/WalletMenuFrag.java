package za.co.botcoin.wallet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import za.co.botcoin.R;
import za.co.botcoin.wallet.fragments.wallet.menu.fragment.OrdersFrag;
import za.co.botcoin.wallet.fragments.wallet.menu.fragment.ReceiveFrag;
import za.co.botcoin.wallet.fragments.wallet.menu.fragment.SendFrag;
import za.co.botcoin.MainActivity;
import za.co.botcoin.utils.FragmentUtils;

public class WalletMenuFrag extends Fragment
{
    private LinearLayoutCompat receiveOption;
    private LinearLayoutCompat sendOption;
    private LinearLayoutCompat ordersOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet_menu, container, false);

        addReceiveListener(view);
        addSendListener(view);
        addOrdersListener(view);

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
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), sendFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Send",true, false, true, null);
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
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), receiveFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Receive",true, false, true, null);
            }
        });
    }

    private void addOrdersListener(View view)
    {
        this.ordersOption = view.findViewById(R.id.linearLayoutOrdersOption);
        this.ordersOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("asset", getArguments().getString("asset"));

                OrdersFrag ordersFrag = new OrdersFrag();
                ordersFrag.setArguments(bundle);
                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), ordersFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Orders",true, false, true, null);
            }
        });
    }
}
