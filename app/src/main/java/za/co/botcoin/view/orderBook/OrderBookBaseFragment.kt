package za.co.botcoin.view.orderBook

import android.content.Context
import androidx.fragment.app.Fragment

abstract class OrderBookBaseFragment(layoutId: Int): Fragment(layoutId) {
    protected lateinit var orderBookActivity: OrderBookActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        orderBookActivity = context as OrderBookActivity
    }
}