package za.co.botcoin.wallet.fragments.wallet.menu.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import za.co.botcoin.R;
import za.co.botcoin.objs.Order;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.StringUtils;
import za.co.botcoin.utils.WSCallUtilsCallBack;
import za.co.botcoin.utils.WSCallsUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class OrdersFrag extends Fragment implements WSCallUtilsCallBack
{
    private final int REQ_CODE_ORDERS = 101;
    private final int REQ_CODE_STOP_ORDER = 102;

    private ListView lvOrders;
    private List<Order> orders;
    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orders, container, false);

        initUI(view);

        getListOrders();

        return view;
    }


    private void getListOrders()
    {
        WSCallsUtils.get(this, REQ_CODE_ORDERS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_LISTORDERS, GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void cancelOrder(String idOrder)
    {
        GeneralUtils.createAlertDialog(getContext(), "Cancel Order", "Are you sure you would like to cancel your order?", true).show();

        String url = StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_STOP_ORDER + "?order_id=" + idOrder;
        WSCallsUtils.post(this, REQ_CODE_STOP_ORDER, url, "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void initUI(View view)
    {
        this.lvOrders = view.findViewById(R.id.lvOrders);
    }

    private void setOrderAdapter(List<Order> orders)
    {
        this.orderAdapter = new OrderAdapter(orders);
    }

    private void setLvOrders(OrderAdapter orderAdapter)
    {
        this.lvOrders.setAdapter(orderAdapter);
    }

    public ListView getLvOrders() {
        return lvOrders;
    }

    public void setLvOrders(ListView lvOrders) {
        this.lvOrders = lvOrders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public OrderAdapter getOrderAdapter() {
        return orderAdapter;
    }

    public void setOrderAdapter(OrderAdapter orderAdapter) {
        this.orderAdapter = orderAdapter;
    }

    private class OrderAdapter extends BaseAdapter
    {
        private List<Order> orders;

        private TextView txtType;
        private TextView txtState;
        private TextView txtLimitPrice;
        private TextView txtLimitVolume;
        private TextView txtPair;
        private TextView txtCreatedTime;
        private TextView txtCompletedTime;
        private Button btnCancel;

        public OrderAdapter(List<Order> orders)
        {
            this.orders = orders;
        }

        @Override
        public int getCount() {
            return this.orders.size();
        }

        @Override
        public Object getItem(int position) {
            return this.orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            if(convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.order_list_item, null);
            }

            initUI(convertView);
            wireUI(position);

            return convertView;
        }

        private void initUI(View view)
        {
            this.txtType = view.findViewById(R.id.txtType);
            this.txtState = view.findViewById(R.id.txtState);
            this.txtLimitPrice = view.findViewById(R.id.txtLimitPrice);
            this.txtLimitVolume = view.findViewById(R.id.txtLimitVolume);
            this.txtPair = view.findViewById(R.id.txtPair);
            this.txtCreatedTime = view.findViewById(R.id.txtCreatedTime);
            this.txtCompletedTime = view.findViewById(R.id.txtCompletedTime);
            this.btnCancel = view.findViewById(R.id.btnCancel);
        }

        private void wireUI(final int position)
        {
            this.txtType.setText(this.orders.get(position).getType());
            this.txtState.setText(this.orders.get(position).getState());
            this.txtLimitPrice.setText(this.orders.get(position).getLimitPrice());
            this.txtLimitVolume.setText(this.orders.get(position).getLimitVolume());
            this.txtPair.setText(this.orders.get(position).getPair());
            this.txtCreatedTime.setText(this.orders.get(position).getCreatedTime());
            this.txtCompletedTime.setText(this.orders.get(position).getCompletedTime());

            if(!this.txtState.getText().toString().equals("COMPLETE"))
            {
                this.btnCancel.setVisibility(View.VISIBLE);
                this.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        cancelOrder(orders.get(position).getId());
                        getListOrders();
                    }
                });
            }else
            {
                this.btnCancel.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(response != null)
        {
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
                            List<Order> orders = new ArrayList<>();

                            for(int i = 0; i < jsonObjectOrders.length(); i++)
                            {
                                JSONObject jsonObjectOrder = jsonObjectOrders.getJSONObject(i);

                                String id = jsonObjectOrder.getString("order_id");
                                String type = jsonObjectOrder.getString("type");
                                String pair = jsonObjectOrder.getString("pair");
                                String state = jsonObjectOrder.getString("state");
                                String limitPrice = jsonObjectOrder.getString("limit_price");
                                String limitVolume = jsonObjectOrder.getString("limit_volume");
                                String createdTime = jsonObjectOrder.getString("creation_timestamp");
                                String completedTime = jsonObjectOrder.getString("completed_timestamp");

                                orders.add(new Order(id, type, state, limitPrice, limitVolume, pair, createdTime, completedTime));
                            }

                            setOrderAdapter(orders);
                            setLvOrders(this.orderAdapter);

                        }
                    }

                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: OrdersFrag - taskCompleted"
                            + "\nRequest Code: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }

            if(reqCode == REQ_CODE_STOP_ORDER)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null)
                    {
                        notify("Order Cancelled", jsonObject.toString());
                    }

                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: OrdersFrag - taskCompleted"
                            + "\nRequest Code: " + reqCode
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
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0,intent,0);
            Notification notification = new Notification.Builder(getContext())
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .addAction(R.drawable.luno_icon,"Action 1", pendingIntent)
                    .setContentIntent(pendingIntent).getNotification();

            notification.flags = Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0,intent,0);

            Notification notification  = new Notification.Builder(getContext())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

    }
}
