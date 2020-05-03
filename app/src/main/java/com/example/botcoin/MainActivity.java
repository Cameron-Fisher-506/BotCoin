package com.example.botcoin;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.botcoin.za.co.botcoin.navigation.fragments.BotFrag;
import com.example.botcoin.za.co.botcoin.navigation.fragments.HomeFrag;
import com.example.botcoin.za.co.botcoin.navigation.fragments.MenuFrag;
import com.example.botcoin.za.co.botcoin.navigation.fragments.WalletFrag;
import com.example.botcoin.za.co.botcoin.settings.fragments.AutoTradeFrag;
import com.example.botcoin.za.co.botcoin.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnHome;
    private ImageButton btnBot;
    private ImageButton btnWallet;
    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        HomeFrag homeFrag = new HomeFrag();
        FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer,  getSupportActionBar(), "Home",true, false, true, null);

        setBtnHomeOnClickListener();
        setBtnBotOnClickListener();
        setBtnWalletOnClickListener();
        setBtnMenuOnClickListener();

        //set to home initially
        setNavIcons(true,false,false,false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.autoTrade:
            {
                //auto trade
                AutoTradeFrag autoTradeFrag = new AutoTradeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), autoTradeFrag, R.id.fragContainer, getSupportActionBar(), "Auto Trade", true, false, true, null);

                break;
            }
            default:
            {
                //unknown
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setBtnHomeOnClickListener()
    {
        this.btnHome = (ImageButton) findViewById(R.id.btnHome);


        this.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(true,false,false,false);

                HomeFrag homeFrag = new HomeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home",true, false, true, null);
            }
        });
    }

    public void setBtnBotOnClickListener()
    {
        this.btnBot = (ImageButton) findViewById(R.id.btnBot);


        this.btnBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false,true,false,false);

                BotFrag botFrag = new BotFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), botFrag, R.id.fragContainer,  getSupportActionBar(), "Trading Bot",true, false, true, null);
            }
        });
    }
    public void setBtnWalletOnClickListener()
    {
        this.btnWallet = (ImageButton) findViewById(R.id.btnWallet);


        this.btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false,false,true,false);

                WalletFrag walletFrag = new WalletFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), walletFrag, R.id.fragContainer,  getSupportActionBar(), "Wallet",true, false, true, null);
            }
        });
    }
    public void setBtnMenuOnClickListener()
    {
        this.btnMenu = (ImageButton) findViewById(R.id.btnMenu);


        this.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false,false,false,true);

                MenuFrag menuFrag = new MenuFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), menuFrag, R.id.fragContainer,  getSupportActionBar(), "Menu",true, false, true, null);
            }
        });
    }

    public void setNavIcons(boolean isHome, boolean isBot, boolean isWallet, boolean isMenu)
    {
        if(isHome)
        {
            this.btnHome.setBackgroundResource(R.drawable.home_fill);
        }else
        {
            this.btnHome.setBackgroundResource(R.drawable.home);
        }

        if(isBot)
        {
            this.btnBot.setBackgroundResource(R.drawable.bot_fill);
        }else
        {
            this.btnBot.setBackgroundResource(R.drawable.bot);
        }

        if(isWallet)
        {
            this.btnWallet.setBackgroundResource(R.drawable.wallet_fill);
        }else
        {
            this.btnWallet.setBackgroundResource(R.drawable.wallet);
        }

        if(isMenu)
        {
            this.btnMenu.setBackgroundResource(R.drawable.menu_fill);
        }else
        {
            this.btnMenu.setBackgroundResource(R.drawable.menu);
        }

    }
}
