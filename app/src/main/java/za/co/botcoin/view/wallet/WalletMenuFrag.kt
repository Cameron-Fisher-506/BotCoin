package za.co.botcoin.view.wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.WalletMenuFragmentBinding
import za.co.botcoin.view.wallet.menu.OrdersFrag
import za.co.botcoin.view.wallet.menu.ReceiveFrag
import za.co.botcoin.view.wallet.menu.SendFrag

class WalletMenuFrag : Fragment(R.layout.wallet_menu_fragment) {
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
        this.binding.linearLayoutSendOption.setOnClickListener {
            val action = WalletMenuFragDirections.actionWalletMenuFragToSendFrag(asset)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addReceiveListener(asset: String) {
        this.binding.linearLayoutReceiveOption.setOnClickListener {
            val action = WalletMenuFragDirections.actionWalletMenuFragToReceiveFrag(asset)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun addOrdersListener(asset: String) {
        this.binding.linearLayoutOrdersOption.setOnClickListener {
           val action = WalletMenuFragDirections.actionWalletMenuFragToOrdersFrag(asset)
            Navigation.findNavController(it).navigate(action)
        }
    }
}