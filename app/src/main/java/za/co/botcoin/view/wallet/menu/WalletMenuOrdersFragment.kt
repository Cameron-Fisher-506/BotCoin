package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import za.co.botcoin.R
import za.co.botcoin.databinding.OrdersFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.view.wallet.WalletBaseFragment

class WalletMenuOrdersFragment : WalletBaseFragment(R.layout.orders_fragment) {
    private lateinit var binding: OrdersFragmentBinding
    private val walletMenuOrdersViewModel by viewModels<WalletMenuOrdersViewModel>(factoryProducer = { walletActivity.getViewModelFactory })
    private lateinit var walletMenuOrderListAdapter: WalletMenuOrderListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = OrdersFragmentBinding.bind(view)

        setUpViews()
        fetchAndObserveOrders()
    }

    private fun setUpViews() {
        walletMenuOrderListAdapter = WalletMenuOrderListAdapter(arrayListOf())
        binding.ordersRecyclerView.adapter = walletMenuOrderListAdapter

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT && walletMenuOrdersViewModel.isOrderStateNotComplete(viewHolder.absoluteAdapterPosition)) {
                    walletMenuOrderListAdapter.cancelOrder("")
                    GeneralUtils.notify(viewHolder.itemView, "Order Cancelled")
                } else {
                    walletMenuOrderListAdapter.updateOrderList(walletViewModel.ordersResponse)
                    GeneralUtils.notify(viewHolder.itemView, "Order Already Completed")
                }
            }
        }
        ItemTouchHelper(simpleCallback).apply { attachToRecyclerView(binding.ordersRecyclerView) }
    }

    private fun fetchAndObserveOrders() {
        walletMenuOrdersViewModel.fetchOrders()
        walletMenuOrdersViewModel.ordersResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    displayOrdersRecyclerView()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        walletViewModel.ordersResponse = walletMenuOrdersViewModel.getSortedOrdersByCreatedTimeDescending(data)
                        walletMenuOrdersViewModel.updateOrdersCreatedAndCreatedTime()
                        walletMenuOrderListAdapter.updateOrderList(walletViewModel.ordersResponse)
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
        walletMenuOrdersViewModel.stopOrderResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    displayOrdersRecyclerView()
                    val data = it.data
                    if (!data.isNullOrEmpty() && data.first().success) {
                        walletMenuOrdersViewModel.displayOrderCancellationSuccessNotification()
                    } else {
                        walletMenuOrdersViewModel.displayOrderCancellationFailureNotification()
                    }
                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayOrdersRecyclerView()
                    walletMenuOrdersViewModel.displayOrderCancellationFailureNotification()
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