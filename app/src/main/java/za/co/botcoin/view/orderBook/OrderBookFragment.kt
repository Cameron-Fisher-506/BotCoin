package za.co.botcoin.view.orderBook

import android.os.Bundle
import android.view.View
import za.co.botcoin.R
import za.co.botcoin.databinding.OrderBookFragmentBinding

class OrderBookFragment : OrderBookBaseFragment(R.layout.order_book_fragment) {
    private lateinit var binding: OrderBookFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = OrderBookFragmentBinding.bind(view)
    }
}