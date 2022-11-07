package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import za.co.botcoin.R
import za.co.botcoin.databinding.OrdersFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.DateTimeUtils
import za.co.botcoin.view.wallet.WalletBaseFragment

class WalletMenuOrdersFragment : WalletBaseFragment(R.layout.orders_fragment) {
    private lateinit var binding: OrdersFragmentBinding
    private val ordersViewModel by viewModels<WalletMenuOrdersViewModel>(factoryProducer = { walletActivity.getViewModelFactory })
    private lateinit var walletMenuOrderListAdapter: WalletMenuOrderListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = OrdersFragmentBinding.bind(view)

        setUpViews()
        fetchAndObserveOrders()
    }

    private fun setUpViews() {
        this.walletMenuOrderListAdapter = WalletMenuOrderListAdapter(arrayListOf())
        this.binding.ordersRecyclerView.adapter = walletMenuOrderListAdapter
    }

    private fun fetchAndObserveOrders() {
        this.ordersViewModel.fetchOrders()
        this.ordersViewModel.ordersResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    displayOrdersRecyclerView()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { order ->
                            order.completedTime = DateTimeUtils.format(order.completedTime.toLong())
                            order.createdTime = DateTimeUtils.format(order.createdTime.toLong())
                        }
                        walletMenuOrderListAdapter.updateOrderList(data.sortedByDescending { order -> order.createdTime })
                    } else {
                        displayErrorTextView()
                    }
                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayErrorTextView()
                }
                Status.LOADING -> {
                    walletActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
    }

    private fun attachStopOrderObserver() {
        this.ordersViewModel.stopOrderResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    displayOrdersRecyclerView()
                    val data = it.data
                    if (!data.isNullOrEmpty() && data.first().success) {
                        ordersViewModel.displayOrderCancellationSuccessNotification()
                    } else {
                        ordersViewModel.displayOrderCancellationFailureNotification()
                    }
                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayOrdersRecyclerView()
                    ordersViewModel.displayOrderCancellationFailureNotification()
                }
                Status.LOADING -> {
                    walletActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
    }

    private fun hideAllViews() {
        this.binding.ordersRecyclerView.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
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
    }
}