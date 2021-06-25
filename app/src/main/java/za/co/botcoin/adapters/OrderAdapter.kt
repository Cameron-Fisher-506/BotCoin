package za.co.botcoin.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import za.co.botcoin.objs.Order;

import java.util.List;

public class OrderAdapter extends BaseAdapter
{

    private List<Order> orders;
    private Context context;

    public OrderAdapter(Context context, List<Order> orders)
    {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
