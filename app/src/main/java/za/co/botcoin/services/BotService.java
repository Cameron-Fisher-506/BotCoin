package za.co.botcoin.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import za.co.botcoin.R;
import za.co.botcoin.objs.Order;
import za.co.botcoin.objs.TradePrice;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.MathUtils;
import za.co.botcoin.utils.StringUtils;
import za.co.botcoin.utils.WSCallUtilsCallBack;
import za.co.botcoin.utils.WSCallsUtils;
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
    private final int REQ_CODE_SEND = 107;
    private final int REQ_CODE_FUNDING_ADDRESS = 108;
    private final int REQ_CODE_ORDERS = 109;
    private final int REQ_CODE_STOP_ORDER = 110;

    private TimerTask timerTask;
    private Timer timer;

    private String currentPrice;

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

    //Orders
    private Order lastAskOrder;
    private Order lastBidOrder;

    //flags
    private int flagSupportPriceCounter = 2;



    public BotService() {
    }

    @Override
    public void onCreate()
    {

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "BotCoin";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "BotCoin",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("BotCoin")
                    .setContentText("BotCoin is auto trading!")
                    .setSmallIcon(R.mipmap.botcoin)
                    .setChannelId(CHANNEL_ID)
                    .build();

            startForeground(1, notification);
        }else
        {
            notify("BotCoin", "BotCoin is auto trading!");
        }

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

                Log.d(ConstantUtils.BOTCOIN_TAG, "AUTO TRADE RUNNING...");
            }
        };

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(this.timerTask, 0, ConstantUtils.TICKER_RUN_TIME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    private void init()
    {
        this.supportPrice = null;
        this.resistancePrice = null;

        this.lastPurchasePrice = "0";
        this.lastPurchaseVolume = "0";
        this.lastTradeType = "";

        this.xrpBalance = "0";
        this.zarBalance = "0";

        this.supportPrices = new ArrayList<>();
        this.resistancePrices = new ArrayList<>();

        this.lastAskOrder = null;
        this.lastBidOrder = null;

    }

    private void getWalletBalance()
    {
        WSCallsUtils.get(this, BALANCE_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_BALANCE, GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void getCurrentPrice()
    {
        WSCallsUtils.get(this, TICKERS_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_TICKERS, GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void bid(boolean isRestrict)
    {
        if(isRestrict)
        {
            if(this.supportPrice != null && !this.lastTradeType.equals(ConstantUtils.TRADE_TYPE_BID) && (Double.parseDouble(this.supportPrice) <  Double.parseDouble(this.currentPrice)))
            {

                Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nMethod: BotService - bid"
                        + "\nsupportPrice: " + this.supportPrice
                        + "\nlastTradeType: " + this.lastTradeType
                        + "\ncurrentPrice: " + this.currentPrice
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

                String amountXrpToBuy = Integer.toString(calcAmountXrpToBuy(Double.parseDouble(this.zarBalance), Double.parseDouble(this.supportPrice)));

                String postOrder = GeneralUtils.buildPostOrder(ConstantUtils.PAIR_XRPZAR, "BID", amountXrpToBuy, this.supportPrice);
                WSCallsUtils.post(this, BUY_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
            }else
            {
                Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nMethod: BotService - bid"
                        + "\nsupportPrice: " + this.supportPrice
                        + "\nlastTradeType: " + this.lastTradeType
                        + "\ncurrentPrice: " + this.currentPrice
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }
        }else
        {
            if(this.supportPrice != null)
            {
                Double percentage = MathUtils.percentage(Double.parseDouble(this.supportPrice), ConstantUtils.trailingStop);
                if(percentage != null)
                {
                    Double precision = MathUtils.precision(percentage);
                    if(precision != null)
                    {
                        Double result = MathUtils.precision(Double.parseDouble(this.supportPrice) + precision);
                        if(this.currentPrice != null && Double.parseDouble(this.currentPrice) >= result)
                        {
                            notify("bid isRestrict: false - (bid reset support: "+this.supportPrice+")", this.currentPrice +">="+ result);
                            this.supportPrice = null;
                        }
                    }

                }
            }
        }


    }

    private int calcAmountXrpToBuy(double zarBalance, double supportPrice)
    {
        int toReturn = 0;

        toReturn = (int) (zarBalance/supportPrice);

        return toReturn;
    }

    private int calcAmountXrpToSell(double xrpBalance)
    {
        int toReturn = 0;

        toReturn = (int) (xrpBalance);

        return toReturn;
    }

    private void ask(Boolean isRestrict)
    {
        boolean placeSellOrder = false;
        String newSellPrice = null;

        if(isRestrict)
        {
            if(this.resistancePrice != null && this.lastTradeType.equals(ConstantUtils.TRADE_TYPE_BID) && (Double.parseDouble(this.resistancePrice) > Double.parseDouble(this.lastPurchasePrice)) && (Double.parseDouble(this.resistancePrice) > Double.parseDouble(this.currentPrice))) //
            {
                placeSellOrder = true;

                Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nMethod: BotService - ask"
                        + "\nresistancePrice: " + this.resistancePrice
                        + "\nlastTradeType: " + this.lastTradeType
                        + "\nlastPurchasePrice: " + this.lastPurchasePrice
                        + "\ncurrentPrice: " + this.currentPrice
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }
        }else
        {
            if(this.resistancePrice != null)
            {
                Double percentage = MathUtils.percentage(Double.parseDouble(this.resistancePrice), ConstantUtils.trailingStop);
                if(percentage != null)
                {
                    Double precision = MathUtils.precision(percentage);
                    if(precision != null)
                    {
                        Double result = MathUtils.precision(Double.parseDouble(this.resistancePrice) - precision);
                        if(this.currentPrice != null && Double.parseDouble(this.currentPrice) < result)
                        {
                            this.resistancePrice = Double.toString(result);
                            placeSellOrder = true;

                            notify("ask - (ResistancePrice: "+this.resistancePrice+")", this.currentPrice +"<"+ result);
                            this.flagSupportPriceCounter = 9;
                        }
                    }

                }
            }else if(this.lastPurchasePrice != null)
            {
                Double percentage = MathUtils.percentage(Double.parseDouble(this.lastPurchasePrice), ConstantUtils.trailingStop);
                if(percentage != null)
                {
                    Double precision = MathUtils.precision(percentage);
                    if(precision != null)
                    {
                        Double result = MathUtils.precision(Double.parseDouble(this.lastPurchasePrice) - precision);
                        if(this.currentPrice != null && Double.parseDouble(this.currentPrice) < result)
                        {
                            newSellPrice = Double.toString(result);
                            placeSellOrder = true;

                            notify("ask - (LastPurchasePrice: "+this.lastPurchasePrice+")", this.currentPrice +"<"+ result);
                            this.flagSupportPriceCounter = 9;
                        }
                    }

                }
            }


        }

        if (placeSellOrder)
        {
            double newXrpBalance = Double.parseDouble(this.xrpBalance);

            /*if (newXrpBalance >= ConstantUtils.SERVICE_FEE_MIN_BALANCE)
            {
                newXrpBalance -= 0.1;
                getBotCoinAccountDetails();
            }*/

            String amountXrpToSell = Integer.toString(calcAmountXrpToSell(newXrpBalance));
            String postOrder = null;
            if(this.resistancePrice != null)
            {
                postOrder = GeneralUtils.buildPostOrder(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell , this.resistancePrice);
            }else if(newSellPrice != null)
            {
                postOrder = GeneralUtils.buildPostOrder(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell , newSellPrice);
            }

            if(postOrder != null)
            {
                WSCallsUtils.post(this, SELL_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
            }else
            {
                Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nMethod: BotService - ask"
                        + "\npostOrder: null"
                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
            }
        }


        Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nMethod: BotService - ask"
                + "\nresistancePrice: " + this.resistancePrice
                + "\nlastTradeType: " + this.lastTradeType
                + "\nlastPurchasePrice: " + this.lastPurchasePrice
                + "\ncurrentPrice: " + this.currentPrice
                + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

    }


    private int getNumberOfPricesCounterMoreThanTwo(List<TradePrice> tradePrices)
    {
        int toReturn = 0;

        for(int i = 0; i < tradePrices.size(); i++)
        {
            if(tradePrices.get(i).getCounter() > this.flagSupportPriceCounter)
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
            StringBuilder prices = new StringBuilder();
            for(int i = 0; i < tradePrices.size(); i++)
            {
                prices.append("["+tradePrices.get(i).getPrice()+", "+tradePrices.get(i).getCounter()+"]\n");
                if(maxCounter == tradePrices.get(i).getCounter() && toReturn > tradePrices.get(i).getPrice())
                {
                    toReturn = tradePrices.get(i).getPrice();
                }
            }
            Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nMethod: BotService - getLowestPriceWithCounter"
                    + "\nPrices: " + prices.toString()
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
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
                if(maxCounter == tradePrices.get(i).getCounter() && toReturn < tradePrices.get(i).getPrice())
                {
                    toReturn = tradePrices.get(i).getPrice();
                }
            }

            Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nMethod: BotService - getHighestPriceWithCounter"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }
        return toReturn;
    }

    private void setSupportPrice()
    {
        if(this.supportPrices != null && this.supportPrices.size() > 0)
        {
            StringBuilder prices = new StringBuilder();
            for(int i = 0; i < this.supportPrices.size(); i++)
            {
                prices.append("["+supportPrices.get(i).getPrice()+", "+supportPrices.get(i).getCounter()+"]\n");
            }

            Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nMethod: BotService - setSupportPrice"
                    + "\nSupportPrices: " + prices.toString()
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }


        //Get the number of prices counter more than 2
        if(getNumberOfPricesCounterMoreThanTwo(this.supportPrices) == 1)
        {
            //Only 1 price with counter > 3
            this.supportPrice = Double.toString(getPriceEqualCounter(this.supportPrices, getMaxCounter(this.supportPrices)));
        }else if(getNumberOfPricesCounterMoreThanTwo(this.supportPrices) > 1)
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

        if(this.resistancePrices != null && this.resistancePrices.size() > 0)
        {
            StringBuilder prices = new StringBuilder();
            for(int i = 0; i < this.resistancePrices.size(); i++)
            {
                prices.append("["+resistancePrices.get(i).getPrice()+", "+resistancePrices.get(i).getCounter()+"]\n");
            }

            Log.d(ConstantUtils.BOTCOIN_TAG, "\n\nMethod: BotService - setResistancePrice"
                    + "\nResistancePrices: " + prices.toString()
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

        //Get the number of prices counter more 2
        if(getNumberOfPricesCounterMoreThanTwo(this.resistancePrices) == 1)
        {
            //Only 1 price with counter > 2
            this.resistancePrice = Double.toString(getPriceEqualCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)));

        }else if(getNumberOfPricesCounterMoreThanTwo(this.resistancePrices) > 1)
        {
            //get the max counter
            if(getNumberOfPricesThatHaveCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)) == 1)
            {
                //choose the price with that maxCounter
                this.resistancePrice = Double.toString(getPriceEqualCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)));

            }else if(getNumberOfPricesThatHaveCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)) > 1)
            {
                //get highest price with counter value
                this.resistancePrice = Double.toString(getHighestPriceWithCounter(this.resistancePrices, getMaxCounter(this.resistancePrices)));
            }
        }

        modifyResistancePrices(this.resistancePrices, Double.parseDouble(this.currentPrice));

    }

    private void getLastPurchase()
    {
        WSCallsUtils.get(this, LISTTRADES_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_LIST_TRADES + GeneralUtils.buildListTrades(ConstantUtils.PAIR_XRPZAR, true), GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void send(String address, String tag)
    {
        WSCallsUtils.post(this,REQ_CODE_SEND, StringUtils.GLOBAL_LUNO_URL + GeneralUtils.buildSend(ConstantUtils.SERVICE_FEE, ConstantUtils.XRP, address, tag), "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void getBotCoinAccountDetails()
    {
        WSCallsUtils.get(this, REQ_CODE_FUNDING_ADDRESS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_FUNDING_ADDRESS + "?asset=" + ConstantUtils.XRP, GeneralUtils.getAuth(ConstantUtils.KEY_ID, ConstantUtils.SECRET_KEY));
    }

    private Double getDifferenceBetweenPrices(Double priceA, Double priceB)
    {
        Double toReturn = null;

        toReturn = priceA - priceB;

        return toReturn;
    }

    private void getListOrders()
    {
        WSCallsUtils.get(this, REQ_CODE_ORDERS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_LISTORDERS, GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void cancelOrder(String idOrder)
    {
        String url = StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_STOP_ORDER + "?order_id=" + idOrder;
        WSCallsUtils.post(this, REQ_CODE_STOP_ORDER, url, "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void pullOutOfAsk(String lastPurchasePrice)
    {
        if(this.lastAskOrder != null)
        {
            Double percentage = MathUtils.percentage(Double.parseDouble(this.lastAskOrder.getLimitPrice()), ConstantUtils.trailingStop);
            if(percentage != null)
            {
                Double precision = MathUtils.precision(percentage);
                if(precision != null)
                {
                    Double result = MathUtils.precision(Double.parseDouble(this.lastAskOrder.getLimitPrice()) - precision);
                    if(this.currentPrice != null && Double.parseDouble(this.currentPrice) < result)
                    {
                        cancelOrder(this.lastAskOrder.getId());

                        notify("pullOutOfAsk - (LastAskOrder: "+this.lastAskOrder.getLimitPrice()+")", this.currentPrice +"<"+ result);
                    }
                }

            }
        }else if(lastPurchasePrice != null && !lastPurchasePrice.equals("0"))
        {
            ask(false);
        }
    }

    private void pullOutOfBid()
    {
        if(this.lastBidOrder != null)
        {
            Double percentage = MathUtils.percentage(Double.parseDouble(this.lastBidOrder.getLimitPrice()), ConstantUtils.trailingStop);
            if(percentage != null)
            {
                Double precision = MathUtils.precision(percentage);
                if(precision != null)
                {
                    Double result = MathUtils.precision(Double.parseDouble(this.lastBidOrder.getLimitPrice()) + precision);
                    if(this.currentPrice != null && Double.parseDouble(this.currentPrice) >= result)
                    {
                        cancelOrder(this.lastBidOrder.getId());

                        notify("pullOutOfBidCancel - (LastBidOrder: "+this.lastBidOrder.getLimitPrice()+")", this.currentPrice +">="+ result);
                    }
                }

            }
        }else if(this.supportPrice != null)
        {
            bid(false);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(this.timer != null)
        {
            this.timer.cancel();
            this.timer.purge();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(response != null)
        {
            if(reqCode == BUY_REQ_CODE)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null && jsonObject.has("order_id"))
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
                    this.flagSupportPriceCounter = 2;

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
                    if(jsonObject != null && jsonObject.has("order_id"))
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

                    if(jsonObject != null && jsonObject.has("tickers"))
                    {
                        JSONArray tickers = jsonObject.getJSONArray("tickers");

                        if(tickers != null && tickers.length() > 0)
                        {
                            for(int i = 0; i < tickers.length(); i++)
                            {
                                JSONObject ticker = tickers.getJSONObject(i);

                                String pair = ticker.getString("pair");

                                if(pair.equals(ConstantUtils.PAIR_XRPZAR))
                                {
                                    this.currentPrice = ticker.getString("last_trade");
                                }
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
                    if(jsonObject != null && jsonObject.has("trades") && !jsonObject.isNull("trades"))
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

                                if(this.currentPrice != null && this.lastPurchasePrice != null && Double.parseDouble(this.currentPrice) > Double.parseDouble(this.lastPurchasePrice))
                                {
                                    setResistancePrice();
                                }

                            }else
                            {
                                setSupportPrice();
                            }
                        }
                    }else
                    {
                        this.lastTradeType = ConstantUtils.TRADE_TYPE_ASK;
                        this.lastPurchasePrice = "0";
                        //set support/resistance price
                        setSupportPrice();
                        //setResistancePrice();
                    }

                    //Get ZAR and XRP balance
                    getWalletBalance();
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
                            bid(true);

                            //sell
                            ask(true);

                            //get all the orders
                            getListOrders();

                        }
                    }else
                    {
                        Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: No Response"
                                + "\nMethod: BotService - onCreate"
                                + "\nURL: " + StringUtils.GLOBAL_LUNO_URL + "/api/1/balance"
                                + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());


                    }
                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: BotService - onCreate"
                            + "\nURL: " + StringUtils.GLOBAL_LUNO_URL + "/api/1/balance"
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }

            if(reqCode == REQ_CODE_FUNDING_ADDRESS)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject != null && jsonObject.has("error"))
                    {
                        GeneralUtils.createAlertDialog(this, "Oops!", jsonObject.getString("error"), false);
                    }else
                    {
                        JSONArray address_meta = jsonObject.getJSONArray("address_meta");
                        String address = null;
                        String tag = null;

                        if(address_meta != null && address_meta.length() > 0)
                        {
                            for(int i = 0; i < address_meta.length(); i++)
                            {
                                JSONObject jsonObjectAddressMeta = address_meta.getJSONObject(i);


                                if(jsonObjectAddressMeta.getString("label").equals("Address"))
                                {
                                    address = jsonObjectAddressMeta.getString("value");
                                }

                                if(jsonObjectAddressMeta.getString("label").equals("XRP Tag"))
                                {
                                    tag = jsonObjectAddressMeta.getString("value");
                                }
                            }

                        }

                        if(address != null && tag != null)
                        {
                            send(address, tag);
                        }

                    }

                }catch (Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: BotService - taskCompleted"
                            + "\nRequest Code: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }

            if(reqCode == REQ_CODE_SEND)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject != null)
                    {
                        notify("Service Fee", jsonObject.toString());
                    }

                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: BotService - taskCompleted"
                            + "\nRequest Code: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }

            if(reqCode == REQ_CODE_ORDERS)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null && jsonObject.has("orders"))
                    {
                        JSONArray jsonObjectOrders = jsonObject.getJSONArray("orders");
                        if(jsonObjectOrders != null && jsonObjectOrders.length() > 0)
                        {
                            for(int i = 0; i < jsonObjectOrders.length(); i++)
                            {
                                JSONObject jsonObjectOrder = jsonObjectOrders.getJSONObject(i);
                                String type = jsonObjectOrder.getString("type");
                                String state = jsonObjectOrder.getString("state");
                                String id = jsonObjectOrder.getString("order_id");
                                String pair = jsonObjectOrder.getString("pair");
                                String limitPrice = jsonObjectOrder.getString("limit_price");
                                String limitVolume = jsonObjectOrder.getString("limit_volume");
                                String createdTime = jsonObjectOrder.getString("creation_timestamp");
                                String completedTime = jsonObjectOrder.getString("completed_timestamp");

                                if(type.equals("ASK") && state.equals("PENDING"))
                                {
                                    this.lastAskOrder = new Order(id, type, state, limitPrice, limitVolume, pair,createdTime, completedTime);
                                }else if(type.equals("BID") && state.equals("PENDING"))
                                {
                                    this.lastBidOrder = new Order(id, type, state, limitPrice, limitVolume, pair,createdTime, completedTime);
                                }
                            }
                        }
                    }

                    //check if pull out is  necessary
                    pullOutOfAsk(this.lastPurchasePrice);
                    pullOutOfBid();

                }catch(Exception e)
                {
                    e.printStackTrace();
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: BotService - taskCompleted"
                            + "\nRequest Code: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }

            if(reqCode == REQ_CODE_STOP_ORDER)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null && jsonObject.has("success"))
                    {
                        notify("Order Cancelled", jsonObject.toString());

                        this.lastAskOrder = null;
                        this.lastBidOrder = null;

                    }

                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: BotService - taskCompleted"
                            + "\nRequest Code: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
        }else
        {
            GeneralUtils.createAlertDialog(this, "No Signal", "Please check your network connection!", false);
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
