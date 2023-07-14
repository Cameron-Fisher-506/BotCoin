package za.co.botcoin.view.wallet.menu

import androidx.recyclerview.widget.DiffUtil
import za.co.botcoin.model.models.Order

class WalletMenOrderListDiffUtil(
    private val oldList: List<Order>,
    private val newList: List<Order>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = false
}