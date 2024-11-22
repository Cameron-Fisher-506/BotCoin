package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import za.co.botcoin.R
import za.co.botcoin.databinding.OrderInformationBottomSheetDialogFragmentBinding
import za.co.botcoin.model.models.Order
import za.co.botcoin.view.wallet.WalletViewModel

class WalletMenuOrderInformationBottomSheetDialogFragment(private val order: Order) : BottomSheetDialogFragment() {
    private lateinit var binding: OrderInformationBottomSheetDialogFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = OrderInformationBottomSheetDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderCreatedTimeInformationLineItemView.setValueTextView(order.createdTime)
        binding.orderCompletedTimeInformationLineItemView.setValueTextView(order.completedTime)
    }
}