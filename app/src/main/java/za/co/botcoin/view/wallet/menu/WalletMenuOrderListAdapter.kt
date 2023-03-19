package za.co.botcoin.view.wallet.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import za.co.botcoin.databinding.OrderListItemBinding
import za.co.botcoin.model.models.Order
import za.co.botcoin.utils.GeneralUtils

class WalletMenuOrderListAdapter(private val ordersList: ArrayList<Order>) : RecyclerView.Adapter<WalletMenuOrderListAdapter.ViewHolder>() {
    class ViewHolder(val binding: OrderListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OrderListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ordersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.typeTextView.text = ordersList[position].type
        holder.binding.pairTextView.text = ordersList[position].pair
        holder.binding.limitPriceTextView.text = ordersList[position].limitPrice
        holder.binding.stateTextView.text = ordersList[position].state
        holder.binding.limitVolumeTextView.text = ordersList[position].limitVolume
        if (holder.binding.stateTextView.text.toString() == "COMPLETE") {
            holder.binding.cancelButton.visibility = View.INVISIBLE
        } else {
            holder.binding.cancelButton.visibility = View.VISIBLE
        }

        holder.binding.cancelButton.setOnClickListener {
            GeneralUtils.createAlertDialog(it.context, "Cancel Order", "Are you sure you would like to cancel your order?", true).show()
            //this.withdrawalViewModel.stopOrder(true, orderId)
        }
    }

    fun updateOrderList(orders: List<Order>) {
        this.ordersList.clear()
        this.ordersList.addAll(orders)
        notifyItemRangeChanged(0, orders.size)
    }
}