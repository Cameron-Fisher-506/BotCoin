package za.co.botcoin.view.wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.WalletMenuFragmentBinding

class WalletMenuFragment : Fragment(R.layout.wallet_menu_fragment) {
    private lateinit var binding: WalletMenuFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = WalletMenuFragmentBinding.bind(view)

        arguments?.let {
            addReceiveListener(it.getString("asset") ?: "")
            addSendListener(it.getString("asset") ?: "")
            addOrdersListener(it.getString("asset") ?: "")
        }
    }

    private fun addSendListener(asset: String) {
        this.binding.sendOptionLinearLayoutCompat.setOnClickListener {
            val action = WalletMenuFragmentDirections.actionWalletMenuFragmentToSendFragment(asset)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addReceiveListener(asset: String) {
        this.binding.receiveOptionLinearLayoutCompat.setOnClickListener {
            val action = WalletMenuFragmentDirections.actionWalletMenuFragmentToReceiveFragment(asset)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addOrdersListener(asset: String) {
        this.binding.ordersOptionLinearLayoutCompat.setOnClickListener {
            val action = WalletMenuFragmentDirections.actionWalletMenuFragmentToOrdersFragment(asset)
            Navigation.findNavController(it).navigate(action)
        }
    }
}