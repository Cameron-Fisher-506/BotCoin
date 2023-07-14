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
        binding = WalletMenuFragmentBinding.bind(view)
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.sendOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(WalletMenuFragmentDirections.actionWalletMenuFragmentToSendFragment())
        }

        binding.receiveOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate(WalletMenuFragmentDirections.actionWalletMenuFragmentToReceiveFragment())
        }

        binding.ordersOptionActionView.setOnClickListener {
            Navigation.findNavController(it).navigate( WalletMenuFragmentDirections.actionWalletMenuFragmentToOrdersFragment())
        }
    }
}