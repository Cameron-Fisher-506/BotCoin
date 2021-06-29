package za.co.botcoin.view.trade

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import za.co.botcoin.model.models.Order

class OrderAdapter(private val context: Context, private val orders: List<Order>) : BaseAdapter() {
    override fun getCount(): Int {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View? {
        return null
    }
}