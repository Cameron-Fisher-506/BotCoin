package za.co.botcoin.menu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import za.co.botcoin.R;
import za.co.botcoin.menu.fragments.donate.menu.fragment.DonateFrag;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.FragmentUtils;
import za.co.botcoin.MainActivity;
public class DonateMenuFrag extends Fragment
{

    private LinearLayoutCompat donateBtcOption;
    private LinearLayoutCompat donateXrpOption;
    private LinearLayoutCompat donateEthOption;
    private LinearLayoutCompat donateLtcOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_donate_menu, container, false);

        addDonateBtcOptionListener(view.findViewById(R.id.linearLayoutDonateBtcOption));
        addDonateXrpOptionListener(view.findViewById(R.id.linearLayoutDonateXrpOption));
        addDonateEthOptionListener(view.findViewById(R.id.linearLayoutDonateEthOption));
        addDonateLtcOptionListener(view.findViewById(R.id.linearLayoutDonateLtcOption));

        return view;
    }

    private void addDonateBtcOptionListener(View view)
    {
        this.donateBtcOption = (LinearLayoutCompat) view;
        this.donateBtcOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Bundle bundle = new Bundle();
                bundle.putString("asset", ConstantUtils.BTC);

                DonateFrag donateFrag = new DonateFrag();
                donateFrag.setArguments(bundle);

                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), donateFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Donate BTC",true, false, true, null);
            }
        });
    }

    private void addDonateXrpOptionListener(View view)
    {
        this.donateXrpOption = (LinearLayoutCompat) view;
        this.donateXrpOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("asset", ConstantUtils.XRP);

                DonateFrag donateFrag = new DonateFrag();
                donateFrag.setArguments(bundle);

                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), donateFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Donate XRP",true, false, true, null);
            }
        });
    }

    private void addDonateEthOptionListener(View view)
    {
        this.donateEthOption = (LinearLayoutCompat) view;
        this.donateEthOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("asset", ConstantUtils.ETH);

                DonateFrag donateFrag = new DonateFrag();
                donateFrag.setArguments(bundle);

                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), donateFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Donate ETH",true, false, true, null);
            }
        });
    }

    private void addDonateLtcOptionListener(View view)
    {
        this.donateLtcOption = (LinearLayoutCompat) view;
        this.donateLtcOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("asset", ConstantUtils.LTC);

                DonateFrag donateFrag = new DonateFrag();
                donateFrag.setArguments(bundle);

                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), donateFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Donate LTC",true, false, true, null);
            }
        });
    }

}
