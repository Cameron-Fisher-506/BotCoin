package za.co.botcoin.view.wallet.menu

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.corelib.databinding.OptionActionMultilineViewBinding
import za.co.botcoin.R
import za.co.botcoin.model.models.Order

class WalletMenuOrderListAdapter(
    private var ordersList: List<Order>,
    private var openBottomSheetDialog: (order: Order) -> Unit
) : RecyclerView.Adapter<WalletMenuOrderListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OptionActionMultilineViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ordersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder.binding) {
        optionActionInformationOneTextView.text = "ZAR"
        if (ordersList[position].type == "BID") {
            optionActionInformationTwoTextView.text = "Buy Coin"
            optionActionIconImageView.setImageResource(R.drawable.xrp)
        } else {
            optionActionInformationTwoTextView.text = "Sell Coin"
            optionActionIconImageView.setImageResource(R.drawable.sa)
        }
        optionActionDescriptionOneTextView.text = ordersList[position].limitPrice
        optionActionDescriptionTwoTextView.text = ordersList[position].limitVolume
        optionActionImageView.visibility = View.GONE

        val backgroundColor = if (ordersList[position].state == "COMPLETE") {
            Color.parseColor("#4bb543")
        } else {
            Color.parseColor("#D81B60")
        }
        formContainer.setBackgroundColor(backgroundColor)

        informationView.setOnClickListener {
            openBottomSheetDialog.invoke(ordersList[position])
        }
    }

    fun cancelOrder(orderId: String) {
        //this.withdrawalViewModel.stopOrder(true, orderId)
        updateOrderList(ordersList)
    }

    fun updateOrderList(newOrdersList: List<Order>) {
        val diffUtilCallback = WalletMenuOrderListDiffUtil(ordersList, newOrdersList)
        val diffResults = DiffUtil.calculateDiff(diffUtilCallback)
        ordersList = newOrdersList
        diffResults.dispatchUpdatesTo(this)
    }

    class ViewHolder(val binding: OptionActionMultilineViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}