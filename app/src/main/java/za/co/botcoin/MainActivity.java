package za.co.botcoin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import za.co.botcoin.policies.DisclaimerPolicyFrag;
import za.co.botcoin.policies.PrivacyPolicyFrag;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.FragmentUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.SharedPreferencesUtils;
import za.co.botcoin.navigation.fragments.TradeFrag;
import za.co.botcoin.navigation.fragments.HomeFrag;
import za.co.botcoin.navigation.fragments.MenuFrag;
import za.co.botcoin.navigation.fragments.WalletFrag;
import za.co.botcoin.services.BotService;
import za.co.botcoin.settings.fragments.AutoTradeFrag;

import org.json.JSONObject;

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

        wireUI();

        displayPrivacyPolicy();
    }

    private void displayPrivacyPolicy()
    {
        try
        {
            JSONObject jsonObject = SharedPreferencesUtils.get(this, SharedPreferencesUtils.PRIVACY_POLICY_ACCEPTANCE);
            if(jsonObject == null)
            {
                setNavIcons(true,false,false,false);
                this.btnHome.setClickable(false);
                this.btnBot.setClickable(false);
                this.btnWallet.setClickable(false);
                this.btnMenu.setClickable(false);

                PrivacyPolicyFrag privacyPolicyFrag = new PrivacyPolicyFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), privacyPolicyFrag, R.id.fragContainer, getSupportActionBar(), "Privacy Policy", true, false, true, null);
                return;
            }

            jsonObject = SharedPreferencesUtils.get(this, SharedPreferencesUtils.DISCLAIMER_ACCEPTANCE);
            if(jsonObject == null)
            {
                setNavIcons(true,false,false,false);
                this.btnHome.setClickable(false);
                this.btnBot.setClickable(false);
                this.btnWallet.setClickable(false);
                this.btnMenu.setClickable(false);

                DisclaimerPolicyFrag disclaimerPolicyFrag = new DisclaimerPolicyFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), disclaimerPolicyFrag, R.id.fragContainer, getSupportActionBar(), "Disclaimer Policy", true, false, true, null);
                return;
            }

            //Pull-out bid price
            saveDefaultPullOutBidPrice();
            setUserPulloutBidPrice();

            HomeFrag homeFrag = new HomeFrag();
            FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer,  getSupportActionBar(), "Home",true, false, true, null);



            //set to home initially
            setNavIcons(true,false,false,false);

            GeneralUtils.runAutoTrade(getApplicationContext());



        }catch(Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - displayPrivacyPolicy"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }


    }

    private void wireUI()
    {
        setBtnHomeOnClickListener();
        setBtnBotOnClickListener();
        setBtnWalletOnClickListener();
        setBtnMenuOnClickListener();
    }

    private void saveDefaultPullOutBidPrice()
    {
        try
        {
            if(SharedPreferencesUtils.get(getApplicationContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT) == null)
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT, ConstantUtils.PULL_OUT_PRICE_DROP);
                SharedPreferencesUtils.save(getApplicationContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_DEFAULT,jsonObject);
            }
        }catch (Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - saveDefaultPullOutBidPrice"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }
    }

    private void setUserPulloutBidPrice()
    {
        try
        {
            if(SharedPreferencesUtils.get(getApplicationContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_USER) != null)
            {
                JSONObject jsonObject = SharedPreferencesUtils.get(getApplicationContext(), SharedPreferencesUtils.PULLOUT_BID_PRICE_USER);
                if(jsonObject != null && jsonObject.has(SharedPreferencesUtils.PULLOUT_BID_PRICE_USER))
                {
                    ConstantUtils.PULL_OUT_PRICE_DROP = jsonObject.getDouble(SharedPreferencesUtils.PULLOUT_BID_PRICE_USER);
                }
            }
        }catch (Exception e)
        {
            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - setUserPulloutBidPrice"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }
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

                TradeFrag tradeFrag = new TradeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), tradeFrag, R.id.fragContainer,  getSupportActionBar(), "Trade",true, false, true, null);
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
