package za.co.botcoin.view.trade

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import za.co.botcoin.databinding.TradeFragmentBinding
import za.co.botcoin.model.models.Trade
import za.co.botcoin.utils.ConstantUtils

class TradeAdapter(var context: Context, var trades: ArrayList<Trade>) : RecyclerView.Adapter<TradeAdapter.ViewHolder>() {
    private lateinit var tradeSharedViewModel: TradeSharedViewModel

    var trade: Trade? = null
    private var zarBalance: Double? = null
    private var currentPrice: Double? = null
    private var xrpBalance: Double? = null

    class ViewHolder(val binding: TradeFragmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TradeFragmentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        trade = trades[position]
        holder.binding.tradeButton.text = trade?.type
        holder.binding.amountEditText.setText(trade?.volume)
        holder.binding.priceEditText.setText(trade?.price)

        holder.binding.tradeButton.setOnClickListener {
            //get balance
        }
    }

    override fun getItemCount(): Int = trades.size

    private fun bid() {
        currentPrice = currentPrice!! - ConstantUtils.BUY_SELL_MARGIN_PRICE
        val amountXrpToBuy = Integer.toString(calcAmountXrpToBuy(zarBalance!!, currentPrice!!))
        //val postOrder = buildPostOrder(ConstantUtils.PAIR_XRPZAR, "BID", amountXrpToBuy, java.lang.Double.toString(currentPrice!!))
        //WSCallsUtils.post(this, REQ_CODE_BUY, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "", getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
    }

    private fun calcAmountXrpToBuy(zarBalance: Double, supportPrice: Double): Int {
        var toReturn = 0
        toReturn = (zarBalance / supportPrice).toInt()
        return toReturn
    }

    private fun calcAmountXrpToSell(xrpBalance: Double): Int {
        var toReturn = 0
        toReturn = xrpBalance.toInt()
        return toReturn
    }

    private fun ask() {
        val amountXrpToSell = Integer.toString(calcAmountXrpToSell(xrpBalance!!))
        currentPrice = currentPrice!! + ConstantUtils.BUY_SELL_MARGIN_PRICE
        //val postOrder = buildPostOrder(ConstantUtils.PAIR_XRPZAR, "ASK", amountXrpToSell, java.lang.Double.toString(currentPrice!!))
        //WSCallsUtils.post(this, REQ_CODE_SELL, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_POSTORDER + postOrder, "", getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY))
    }
}