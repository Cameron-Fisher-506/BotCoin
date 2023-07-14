package za.co.botcoin.view.wallet.menu

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import za.co.botcoin.databinding.OrderListItemBinding
import za.co.botcoin.model.models.Order

class WalletMenuOrderListAdapter(private var ordersList: List<Order>) : RecyclerView.Adapter<WalletMenuOrderListAdapter.ViewHolder>() {
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
        val backgroundColor = if (holder.binding.stateTextView.text.toString() == "COMPLETE") {
            Color.parseColor("#4bb543")
        } else {
            Color.parseColor("#D81B60")
        }
        holder.binding.formContainer.setBackgroundColor(backgroundColor)
    }

    fun cancelOrder(orderId: String) {
        //this.withdrawalViewModel.stopOrder(true, orderId)
        updateOrderList(ordersList)
    }

    fun updateOrderList(newOrdersList: List<Order>) {
        val diffUtilCallback = WalletMenOrderListDiffUtil(ordersList, newOrdersList)
        val diffResults = DiffUtil.calculateDiff(diffUtilCallback)
        ordersList = newOrdersList
        diffResults.dispatchUpdatesTo(this)
    }

    class ViewHolder(val binding: OrderListItemBinding) : RecyclerView.ViewHolder(binding.root)
}