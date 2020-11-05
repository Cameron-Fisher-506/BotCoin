package za.co.botcoin.viewpageradapters;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.co.botcoin.R;
import za.co.botcoin.objs.Trade;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.StringUtils;
import za.co.botcoin.utils.WSCallUtilsCallBack;
import za.co.botcoin.utils.WSCallsUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.BotViewHolder> implements WSCallUtilsCallBack
{
    private final int REQ_CODE_BUY = 101;
    private final int REQ_CODE_SELL = 102;
    public final int REQ_CODE_BALANCE = 103;

    private Context context;
    List<Trade> trades;
    Trade trade;

    private Double zarBalance;
    private Double currentPrice;
    private Double xrpBalance;

    public TradeAdapter(Context context, List<Trade> trades)
    {
        this.context = context;
        this.trades = trades;
    }

    @NonNull
    @Override
    public BotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(this.context).inflate(R.layout.frag_trade, parent, false);
        return new BotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BotViewHolder holder, int position)
    {
        this.trade = this.trades.get(position);

        holder.btnTrade.setText(trade.getType());
        holder.txtAmount.setText(trade.getAmount());
        holder.txtPrice.setText(trade.getPrice());

    }

    @Override
    public int getItemCount() {
        return this.trades.size();
    }

    public class BotViewHolder extends RecyclerView.ViewHolder{

        public Button btnTrade;
        public EditText txtAmount;
        public EditText txtPrice;

        public BotViewHolder(View view)
        {
            super(view);

            this.btnTrade = view.findViewById(R.id.btnTrade);
            this.txtAmount = view.findViewById(R.id.txtAmount);
            this.txtPrice = view.findViewById(R.id.txtPrice);

            addBtnTradeListener(view);
        }

        private void addBtnTradeListener(View view)
        {
            this.btnTrade = view.findViewById(R.id.btnTrade);
            this.btnTrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    getBalance();
                }
            });
        }
    }


    private void getBalance()
    {
        WSCallsUtils.get(this, REQ_CODE_BALANCE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_BALANCE, GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void bid()
    {
        this.currentPrice = this.currentPrice - ConstantUtils.BUY_SELL_MARGIN_PRICE;
        String amountXrpToBuy = Integer.toString(calcAmountXrpToBuy(this.zarBalance, this.currentPrice));

        String postOrder = GeneralUtils.buildPostOrder(ConstantUtils.PAIR_XRPZAR, "BID", amountXrpToBuy, Double.toString(this.currentPrice));
        WSCallsUtils.post(this, REQ_CODE_BUY, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));

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

    private void ask()
    {
        String amountXrpToSell = Integer.toString(calcAmountXrpToSell(this.xrpBalance));
        this.currentPrice = this.currentPrice + ConstantUtils.BUY_SELL_MARGIN_PRICE;

        String postOrder = GeneralUtils.buildPostOrder(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell , Double.toString(this.currentPrice));
        WSCallsUtils.post(this, REQ_CODE_SELL, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    @Override
    public void taskCompleted(String response, int reqCode) {
        if(response != null)
        {
            if(reqCode == REQ_CODE_BUY)
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



                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: TradeAdapter - taskCompleted"
                            + "\nreqCode: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

                }
            }

            if(reqCode == REQ_CODE_SELL)
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


                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: TradeAdapter - taskCompleted"
                            + "\nreqCode: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());

                }
            }

            if(reqCode == REQ_CODE_BALANCE)
            {
                try
                {

                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null && jsonObject.has("balance"))
                    {
                        try
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
                                        this.xrpBalance = Double.parseDouble(balance);

                                    }else if(currency.equals(ConstantUtils.ZAR))
                                    {
                                        this.zarBalance = Double.parseDouble(balance);
                                    }

                                }
                            }

                            if(trade.getType().equals("BUY"))
                            {
                                bid();
                            }else if(trade.getType().equals("SELL"))
                            {
                                ask();
                            }
                        }catch(Exception e)
                        {
                            Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                                    + "\nMethod: TradeAdapter - taskCompleted"
                                    + "\nURL: " + StringUtils.GLOBAL_LUNO_URL + "/api/1/balance"
                                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                        }


                    }else
                    {
                        Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: No Response"
                                + "\nMethod: TradeAdapter - taskCompleted"
                                + "\nURL: " + StringUtils.GLOBAL_LUNO_URL + "/api/1/balance"
                                + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());


                    }


                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: TradeAdapter - onCreate"
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }

        }else
        {

        }
    }

    public void notify(String title, String message)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0,intent,0);
            Notification notification = new Notification.Builder(this.context)
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .addAction(R.drawable.luno_icon,"Action 1", pendingIntent)
                    .setContentIntent(pendingIntent).getNotification();

            notification.flags = Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager)this.context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0,intent,0);

            Notification notification  = new Notification.Builder(this.context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager)this.context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

    }
}
