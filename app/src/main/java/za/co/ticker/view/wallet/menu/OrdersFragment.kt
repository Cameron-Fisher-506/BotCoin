package za.co.ticker.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import za.co.ticker.R
import za.co.ticker.databinding.OrdersFragmentBinding
import za.co.ticker.enum.Status
import za.co.ticker.utils.GeneralUtils
import za.co.ticker.view.wallet.WithdrawalViewModel
import java.util.*

class OrdersFragment : Fragment(R.layout.orders_fragment) {
    private lateinit var binding: OrdersFragmentBinding
    private lateinit var withdrawalViewModel: WithdrawalViewModel
    private lateinit var orderListAdapter: OrderListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = OrdersFragmentBinding.bind(view)

        this.withdrawalViewModel = ViewModelProviders.of(this).get(WithdrawalViewModel::class.java)
        wireUI()

        this.withdrawalViewModel.fetchOrders()
        attachOrdersObserver()
    }

    private fun wireUI() {
        this.orderListAdapter = OrderListAdapter(arrayListOf())
        this.binding.ordersRecyclerView.layoutManager = GridLayoutManager(context, 1)
        this.binding.ordersRecyclerView.adapter = orderListAdapter
    }

    private fun attachOrdersObserver() {
        this.withdrawalViewModel.ordersLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    displayOrdersRecyclerView()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        orderListAdapter.updateOrderList(data)
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
        this.withdrawalViewModel.stopOrderLiveData.observe(viewLifecycleOwner, {
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