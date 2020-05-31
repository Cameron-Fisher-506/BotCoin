package com.example.botcoin.za.co.botcoin.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.objs.Trade;
import com.example.botcoin.za.co.botcoin.objs.TradePrice;
import com.example.botcoin.za.co.botcoin.utils.ConstantUtils;
import com.example.botcoin.za.co.botcoin.utils.DTUtils;
import com.example.botcoin.za.co.botcoin.utils.GeneralUtils;
import com.example.botcoin.za.co.botcoin.utils.StringUtils;
import com.example.botcoin.za.co.botcoin.utils.WSCallUtilsCallBack;
import com.example.botcoin.za.co.botcoin.utils.WSCallsUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BotService extends Service implements WSCallUtilsCallBack
{

    private final int BUY_REQ_CODE = 101;
    private final int SELL_REQ_CODE = 102;
    private final int TICKERS_REQ_CODE = 103;
    private final int LISTTRADES_REQ_CODE = 104;
    private final int BALANCE_REQ_CODE = 105;
    private final int REQ_CODE_LAST_TRADE_TYPE = 106;

    private TimerTask timerTask;
    private Timer timer;

    private String currentPrice;
    private boolean isCurrentPriceIncreased;

    //LAST PURCHASE
    private String lastPurchasePrice;
    private String lastPurchaseVolume;
    private String lastTradeType;

    //Support/Resistance
    private String supportPrice;
    private String resistancePrice;

    //Support and Resistance Prices
    List<TradePrice> supportPrices;
    List<TradePrice> resistancePrices;

    //wallet
    private String xrpBalance;
    private String zarBalance;


    public BotService() {
    }

    @Override
    public void onCreate()
    {
        //initialise values
        init();
        this.timerTask = new TimerTask() {
            @Override
            public void run()
            {
                //get current price
                getCurrentPrice();

                //getLastPurchase
                getLastPurchase();

                //Get ZAR and XRP balance
                getWalletBalance();

                Log.d(ConstantUtils.BOTCOIN_TAG, "AUTO TRADE RUNNING...");
            }
        };

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(this.timerTask, 0, ConstantUtils.TICKER_RUN_TIME);
    }



    private void init()
    {
        this.isCurrentPriceIncreased = false;

        this.supportPrice = null;
        this.resistancePrice = null;

        this.lastPurchasePrice = "0";
        this.lastPurchaseVolume = "0";
        this.lastTradeType = "";

        this.xrpBalance = "0";
        this.zarBalance = "0";

        this.supportPrices = new ArrayList<>();
        this.resistancePrices = new ArrayList<>();

    }

    private void getWalletBalance()
    {
        WSCallsUtils.get(this, BALANCE_REQ_CODE,StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_BALANCE);
    }

    private void getCurrentPrice()
    {
        WSCallsUtils.get(this, TICKERS_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_TICKERS);
    }

    private void bid()
    {
        if(this.supportPrice != null && !this.lastTradeType.equals(ConstantUtils.TRADE_TYPE_BID))
        {
            String amountXrpToBuy = Integer.toString(calcAmountXrpToBuy(Double.parseDouble(this.zarBalance), Double.parseDouble(this.supportPrice)));

            String postOrder = GeneralUtils.buildPostOrder(ConstantUtils.PAIR_XRPZAR, "BID", amountXrpToBuy, this.supportPrice);
            WSCallsUtils.post(this, BUY_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "");
        }

    }

    private int calcAmountXrpToBuy(double zarBalance, double supportPrice)
    {
        int toReturn = 0;

        toReturn = (int) (zarBalance/supportPrice);

        return toReturn;
    }

    private void ask()
    {
        if(this.resistancePrice != null && this.lastTradeType.equals(ConstantUtils.TRADE_TYPE_BID) && (Double.parseDouble(this.currentPrice) > Double.parseDouble(this.lastPurchasePrice)))
        {
            String postOrder = GeneralUtils.buildPostOrder(ConstantUtils.PAIR_XRPZAR, "ASK", this.xrpBalance , this.resistancePrice);
            WSCallsUtils.post(this, SELL_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "");
        }

    }


    private int getNumberOfPricesCounterMoreThanEqualThree(List<TradePrice> tradePrices)
    {
        int toReturn = 0;

        for(int i = 0; i < tradePrices.size(); i++)
        {
            if(tradePrices.get(i).getCounter() >= 3)
            {
                toReturn++;
            }
        }

        return toReturn;
    }

    private int getMaxCounter(List<TradePrice> tradePrices)
    {
        int toReturn = 0;

        if(tradePrices != null && tradePrices.size() > 0)
        {
            toReturn = tradePrices.get(0).getCounter();

            for(int i = 0; i < tradePrices.size(); i++)
            {
                if(toReturn < tradePrices.get(i).getCounter())
                {
                    toReturn = tradePrices.get(i).getCounter();
                }
            }
        }

        return toReturn;
    }

    private int getNumberOfPricesThatHaveCounter(List<TradePrice> tradePrices, int counter)
    {
        int toReturn = 0;

        if(tradePrices != null && tradePrices.size() > 0)
        {
            for(int i = 0; i < tradePrices.size(); i++)
            {
                if(tradePrices.get(i).getCounter() == counter)
                {
                    toReturn++;
                }
            }
        }

        return toReturn;
    }

    private double getPriceEqualCounter(List<TradePrice> tradePrices, int counter)
    {
        double toReturn = 0;

        if(tradePrices != null && tradePrices.size() > 0)
        {
            for(int i = 0; i < tradePrices.size(); i++)
            {
                if(tradePrices.get(i).getCounter() == counter)
                {
                    toReturn = tradePrices.get(i).getPrice();
                }
            }
        }

        return toReturn;
    }

    private void modifySupportPrices(List<TradePrice> supportPrices, Double currentPrice)
    {
        addPriceToList(supportPrices, currentPrice, false);

        //check if the current price increases above the temp support price
        if(supportPrices != null && supportPrices.size() > 0)
        {
            for(int i = 0; i < supportPrices.size(); i++)
            {
                if(currentPrice > supportPrices.get(i).getPrice())
                {
                    supportPrices.get(i).setIncreased(true);
                }

                if(currentPrice.doubleValue() == supportPrices.get(i).getPrice().doubleValue())
                {
                    if(supportPrices.get(i).getIncreased())
                    {
                        int currentCounter = supportPrices.get(i).getCounter() + 1;
                        supportPrices.get(i).setCounter(currentCounter);

                        supportPrices.get(i).setIncreased(false);
                    }

                }
            }
        }
    }

    private void modifyResistancePrices(List<TradePrice> resistancePrices, Double currentPrice)
    {
        addPriceToList(resistancePrices, currentPrice, true);

        //check if the current price increases above the temp support price
        if(resistancePrices != null && resistancePrices.size() > 0)
        {
            for(int i = 0; i < resistancePrices.size(); i++)
            {
                if(currentPrice < resistancePrices.get(i).getPrice())
                {
                    resistancePrices.get(i).setIncreased(false);
                }

                if(currentPrice.doubleValue() == resistancePrices.get(i).getPrice().doubleValue())
                {
                    if(!resistancePrices.get(i).getIncreased())
                    {
                        int currentCounter = resistancePrices.get(i).getCounter() + 1;
                        resistancePrices.get(i).setCounter(currentCounter);

                        resistancePrices.get(i).setIncreased(true);
                    }

                }
            }
        }
    }

    private void addPriceToList(List<TradePrice> tradePrices, Double currentPrice, Boolean isIncreased)
    {
        if(tradePrices != null && tradePrices.size() > 0)
        {
            for(int i = 0; i < tradePrices.size(); i++)
            {
                if(currentPrice.doubleValue() == tradePrices.get(i).getPrice().doubleValue())
                {
                    return;
                }
            }

            //price is not in the list
            tradePrices.add(new TradePrice(currentPrice, isIncreased));
        }else if(tradePrices != null)
        {
            tradePrices.add(new TradePrice(currentPrice, isIncreased));
        }
    }

    private Double getLowestPriceWithCounter(List<TradePrice> tradePrices, int maxCounter)
    {
        Double toReturn = null;

        if(tradePrices != null && tradePrices.size() > 0)
        {
            toReturn = tradePrices.get(0).getPrice();
            for(int i = 0; i < tradePrices.size(); i++)
            {
                if(maxCounter == tradePrices.get(i).getCounter() && toReturn > tradePrices.get(i).getPrice())
                {
                    toReturn = tradePrices.get(i).getPrice();
                }
            }
        }

        return toReturn;
    }


    private Double getHighestPriceWithCounter(List<TradePrice> tradePrices, int maxCounter)
    {
        Double toReturn = null;

        if(tradePrices != null && tradePrices.size() > 0)
        {
            toReturn = tradePrices.get(0).getPrice();
            for(int i = 0; i < tradePrices.size(); i++)
            {
                if(maxCounter == tradePrices.get(0).getCounter() && toReturn < tradePrices.get(0).getPrice())
                {
                    toReturn = tradePrices.get(0).getPrice();
                }
            }
        }

        return toReturn;
    }

    private void setSupportPrice()
    {
        //Get the number of prices counter more than equal to 3
        if(getNumberOfPricesCounterMoreThanEqualThree(this.supportPrices) == 1)
        {
            //Only 1 price with counter > 3
            this.supportPrice = Double.toString(getPriceEqualCounter(this.supportPrices, getMaxCounter(this.supportPrices)));

        }else if(getNumberOfPricesCounterMoreThanEqualThree(this.supportPrices) > 1)
        {
            //get the max counter
            if(getNumberOfPricesThatHaveCounter(this.supportPrices, getMaxCounter(this.supportPrices)) == 1)
            {
                //choose the price with that maxCounter
                this.supportPrice = Double.toString(getPriceEqualCounter(this.supportPrices, getMaxCounter(this.supportPrices)));

            }else if(getNumberOfPricesThatHaveCounter(this.supportPrices, getMaxCounter(this.supportPrices)) > 1)
            {
                //get lowest price with counter value
                this.supportPrice = Double.toString(getLowestPriceWithCounter(this.supportPrices, getMaxCounter(this.supportPrices)));
            }
        }

        modifySupportPrices(this.supportPrices, Double.parseDouble(this.currentPrice));

    }

    private void setResistancePrice()
    {

        //Get the number of prices counter more than equal to 3
        if(getNumberOfPricesCounterMoreThanEqualThree(this.resistancePrices) == 1)
        {
            //Only 1 price with counter > 3
            this.resistancePrice = Double.toString(getPriceEqualCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)));

        }else if(getNumberOfPricesCounterMoreThanEqualThree(this.resistancePrices) > 1)
        {
            //get the max counter
            if(getNumberOfPricesThatHaveCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)) == 1)
            {
                //choose the price with that maxCounter
                this.resistancePrice = Double.toString(getPriceEqualCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)));

            }else if(getNumberOfPricesThatHaveCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)) > 1)
            {
                //get lowest price with counter value
                this.resistancePrice = Double.toString(getHighestPriceWithCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)));
            }
        }

        modifyResistancePrices(this.resistancePrices, Double.parseDouble(this.currentPrice));

    }

    private void getLastPurchase()
    {
        WSCallsUtils.get(this, LISTTRADES_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_LIST_TRADES + GeneralUtils.buildListTrades(ConstantUtils.PAIR_XRPZAR, true));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.timer.cancel();
        this.timer.purge();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(reqCode == BUY_REQ_CODE)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.has("order_id"))
                {
                    notify("Auto Trade", "New buy order has been placed."
                                + "\nOrderId: " + jsonObject.getString("order_id"));

                }else if(jsonObject.has("error"))
                {
                    notify("Auto Trade", jsonObject.getString("error"));
                }

                //empty the the trade price list
                this.supportPrice = null;
                this.supportPrices.clear();

            }catch(Exception e)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                        + "\nMethod: BotService - taskCompleted"
                        + "\nreqCode: " + reqCode
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

            }

        }

        if(reqCode == SELL_REQ_CODE)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.has("order_id"))
                {
                    notify("Auto Trade", "New sell order has been placed."
                            + "\nOrderId: " + jsonObject.getString("order_id"));

                }else if(jsonObject.has("error"))
                {
                    notify("Auto Trade", jsonObject.getString("error"));
                }

                //empty the the trade price list
                this.resistancePrice = null;
                this.resistancePrices.clear();
            }catch(Exception e)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                        + "\nMethod: BotService - taskCompleted"
                        + "\nreqCode: " + reqCode
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

            }
        }

        if(reqCode == TICKERS_REQ_CODE)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray tickers = jsonObject.getJSONArray("tickers");

                if(tickers != null && tickers.length() > 0)
                {
                    for(int i = 0; i < tickers.length(); i++)
                    {
                        JSONObject ticker = tickers.getJSONObject(i);

                        String pair = ticker.getString("pair");
                        final String lastTrade = ticker.getString("last_trade");
                        if(pair.equals(ConstantUtils.PAIR_XRPZAR))
                        {
                            this.currentPrice = lastTrade;
                        }
                    }
                }
            }catch(Exception e)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                        + "\nMethod: BotService - taskCompleted"
                        + "\nreqCode: " + reqCode
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

            }
        }

        if(reqCode == LISTTRADES_REQ_CODE)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject != null && jsonObject.has("trades"))
                {
                    JSONArray trades = jsonObject.getJSONArray("trades");
                    if(trades != null && trades.length() > 0)
                    {
                        JSONObject trade = trades.getJSONObject(0);
                        String type = trade.getString("type");

                        this.lastTradeType = type;
                        if(this.lastTradeType.equals(ConstantUtils.TRADE_TYPE_BID))
                        {
                            this.lastPurchasePrice = trade.getString("price");
                            this.lastPurchaseVolume = trade.getString("volume");
                        }

                        //set support/resistance price
                        setSupportPrice();
                        setResistancePrice();

                    }
                }
            }catch(Exception e)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                        + "\nMethod: BotService - taskCompleted"
                        + "\nreqCode: " + reqCode
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

            }

        }

        if(reqCode == BALANCE_REQ_CODE)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject != null && jsonObject.has("balance"))
                {
                    JSONArray jsonArrayBalance = jsonObject.getJSONArray("balance");
                    if(jsonArrayBalance != null && jsonArrayBalance.length() > 0)
                    {
                        for(int i = 0; i < jsonArrayBalance.length(); i++)
                        {
                            JSONObject jsonObjectBalance = jsonArrayBalance.getJSONObject(i);

                            String currency = jsonObjectBalance.getString("asset");
                            String balance = jsonObjectBalance.getString("balance");
                            String reserved = jsonObjectBalance.getString("reserved");

                            if(currency.equals(ConstantUtils.XRP))
                            {
                                this.xrpBalance = balance;

                            }else if(currency.equals(ConstantUtils.ZAR))
                            {
                                this.zarBalance = balance;
                            }
                        }

                        //buy
                        bid();

                        //sell
                        ask();
                    }
                }else
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: No Response"
                            + "\nMethod: MainActivity - onCreate"
                            + "\nURL: " + StringUtils.GLOBAL_LUNO_URL + "/api/1/balance"
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());


                }
            }catch(Exception e)
            {
                Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                        + "\nMethod: MainActivity - onCreate"
                        + "\nURL: " + StringUtils.GLOBAL_LUNO_URL + "/api/1/balance"
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

            }
        }
    }

    public void notify(String title, String message)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,0);
            Notification notification = new Notification.Builder(this)
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .addAction(R.drawable.luno_icon,"Action 1", pendingIntent)
                    .setContentIntent(pendingIntent).getNotification();

            notification.flags = Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,0);

            Notification notification  = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

    }
}
