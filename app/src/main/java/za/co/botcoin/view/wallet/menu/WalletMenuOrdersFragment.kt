package za.co.botcoin.view.wallet.menu

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import za.co.botcoin.R
import za.co.botcoin.databinding.OrdersFragmentBinding
import za.co.botcoin.state.ServiceState
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.view.wallet.WalletBaseFragment
import kotlin.math.abs

class WalletMenuOrdersFragment : WalletBaseFragment(R.layout.orders_fragment) {
    private lateinit var binding: OrdersFragmentBinding
    private val walletMenuOrdersViewModel by viewModels<WalletMenuOrdersViewModel>(factoryProducer = { walletActivity.getViewModelFactory })
    private lateinit var walletMenuOrderListAdapter: WalletMenuOrderListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = OrdersFragmentBinding.bind(view)
        setUpViews()
        fetchAndObserveOrders()
    }

    private fun setUpViews() {
        walletMenuOrderListAdapter = WalletMenuOrderListAdapter(arrayListOf()) {
            val bottomSheetFragment = WalletMenuOrderInformationBottomSheetDialogFragment(it)
            bottomSheetFragment.show(parentFragmentManager, "BSDialogFragment")
        }
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

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                val directionRight = 1
                val directionLeft = 0

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
                    val direction = if (dX > 0) directionRight else directionLeft
                    val absoluteDisplacement = abs(dX)

                    if (direction == directionLeft) {
                        val itemView = viewHolder.itemView
                        val backgroundColor = ColorDrawable()
                        backgroundColor.color = Color.RED
                        backgroundColor.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
                        backgroundColor.draw(c)

                        val icon = ActivityCompat.getDrawable(walletActivity, R.drawable.ic_baseline_cancel_24)
                        if (icon != null) {
                            val top = ((itemView.height / 2) - (icon.intrinsicHeight / 2)) + itemView.top
                            icon.setBounds(((0 - icon.intrinsicWidth) + absoluteDisplacement).toInt(), top, (0 + absoluteDisplacement).toInt(), top + icon.intrinsicHeight)
                            icon.draw(c)
                        }
                    }
                }
            }
        }
        ItemTouchHelper(simpleCallback).apply { attachToRecyclerView(binding.ordersRecyclerView) }
    }

    private fun fetchAndObserveOrders() {
        walletMenuOrdersViewModel.fetchOrders()
        walletMenuOrdersViewModel.ordersResponse.observe(viewLifecycleOwner) {
            when (it.serviceState) {
                ServiceState.Success -> {
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
                ServiceState.Error -> {
                    walletActivity.dismissProgressBar()
                    displayErrorTextView()
                }
                ServiceState.Loading -> {
                    walletActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
    }

    private fun attachStopOrderObserver() {
        walletMenuOrdersViewModel.stopOrderResponse.observe(viewLifecycleOwner) {
            when (it.serviceState) {
                ServiceState.Success -> {
                    walletActivity.dismissProgressBar()
                    displayOrdersRecyclerView()
                    val data = it.data
                    if (!data.isNullOrEmpty() && data.first().success) {
                        walletMenuOrdersViewModel.displayOrderCancellationSuccessNotification()
                    } else {
                        walletMenuOrdersViewModel.displayOrderCancellationFailureNotification()
                    }
                }
                ServiceState.Error -> {
                    walletActivity.dismissProgressBar()
                    displayOrdersRecyclerView()
                    walletMenuOrdersViewModel.displayOrderCancellationFailureNotification()
                }
                ServiceState.Loading -> {
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