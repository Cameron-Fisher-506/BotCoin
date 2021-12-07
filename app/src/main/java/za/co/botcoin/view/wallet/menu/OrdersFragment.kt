package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import za.co.botcoin.R
import za.co.botcoin.databinding.OrdersFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.model.repository.order.OrderViewModel
import za.co.botcoin.model.repository.stopOrder.StopOrderViewModel
import za.co.botcoin.utils.DateTimeUtils
import za.co.botcoin.utils.GeneralUtils

class OrdersFragment : Fragment(R.layout.orders_fragment) {
    private lateinit var binding: OrdersFragmentBinding
    private lateinit var stopOrderViewModel: StopOrderViewModel
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderListAdapter: OrderListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = OrdersFragmentBinding.bind(view)

        this.orderViewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        this.stopOrderViewModel = ViewModelProviders.of(this).get(StopOrderViewModel::class.java)
        wireUI()
        fetchAndObserveOrders()
    }

    private fun wireUI() {
        this.orderListAdapter = OrderListAdapter(arrayListOf())
        this.binding.ordersRecyclerView.layoutManager = GridLayoutManager(context, 1)
        this.binding.ordersRecyclerView.adapter = orderListAdapter
    }

    private fun fetchAndObserveOrders() {
        this.orderViewModel.fetchOrders()
        this.orderViewModel.ordersLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    displayOrdersRecyclerView()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { order ->
                            order.completedTime = DateTimeUtils.format(order.completedTime.toLong())
                            order.createdTime = DateTimeUtils.format(order.createdTime.toLong())
                        }
                        val sortedOrders = data.sortedByDescending { order -> order.createdTime }
                        orderListAdapter.updateOrderList(sortedOrders)
                    } else {
                        displayErrorTextView()
                    }
                }
                Status.ERROR -> {
                    displayErrorTextView()
                }
                Status.LOADING -> {
                    displayProgressBar()
                }
            }
        })
    }

    private fun attachStopOrderObserver() {
        this.stopOrderViewModel.stopOrderLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    displayOrdersRecyclerView()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        if (data.first().success) GeneralUtils.notify(context,"Order Cancellation", "Order cancelled successfully.") else GeneralUtils.notify(context,"Order Cancellation", "Order cancellation failed.")
                    } else {
                        GeneralUtils.notify(context,"Order Cancellation", "Order cancellation failed.")
                    }
                }
                Status.ERROR -> {
                    displayOrdersRecyclerView()
                    GeneralUtils.notify(context,"Order Cancellation", "Order cancellation failed.")
                }
                Status.LOADING -> {
                    displayProgressBar()
                }
            }
        })
    }

    private fun hideAllViews() {
        this.binding.ordersRecyclerView.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
        this.binding.progressBar.visibility = View.GONE
    }

    private fun displayOrdersRecyclerView() {
        hideAllViews()
        this.binding.ordersRecyclerView.visibility = View.VISIBLE
    }

    private fun displayErrorTextView() {
        hideAllViews()
        this.binding.errorTextView.visibility = View.VISIBLE
    }

    private fun displayProgressBar() {
        hideAllViews()
        this.binding.progressBar.visibility = View.VISIBLE
    }
}