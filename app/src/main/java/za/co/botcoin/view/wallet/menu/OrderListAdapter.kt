package za.co.botcoin.view.wallet.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import za.co.botcoin.databinding.OrderListItemBinding
import za.co.botcoin.model.models.Order
import za.co.botcoin.utils.GeneralUtils

class OrderListAdapter(private val ordersList: ArrayList<Order>) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    class ViewHolder(val binding: OrderListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OrderListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ordersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtPair.text = ordersList[position].pair
        holder.binding.txtCompletedTime.text = ordersList[position].completedTime
        holder.binding.txtCreatedTime.text = ordersList[position].createdTime
        holder.binding.txtLimitPrice.text = ordersList[position].limitPrice
        holder.binding.txtState.text = ordersList[position].state
        holder.binding.txtLimitVolume.text = ordersList[position].limitVolume
        if (holder.binding.txtState.text.toString() == "COMPLETE") {
            holder.binding.btnCancel.visibility = View.INVISIBLE
        } else {
            holder.binding.btnCancel.visibility = View.VISIBLE
        }

        holder.binding.btnCancel.setOnClickListener {
            GeneralUtils.createAlertDialog(it.context, "Cancel Order", "Are you sure you would like to cancel your order?", true)?.show()
            //this.withdrawalViewModel.stopOrder(true, orderId)
        }
    }

    fun updateOrderList(orders: List<Order>) {
        this.ordersList.clear()
        this.ordersList.addAll(orders)
        notifyDataSetChanged()
    }
}