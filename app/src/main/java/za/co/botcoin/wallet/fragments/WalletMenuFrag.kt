package za.co.botcoin.wallet.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import za.co.botcoin.MainActivity
import za.co.botcoin.R
import za.co.botcoin.databinding.WalletMenuFragmentBinding
import za.co.botcoin.utils.FragmentUtils.startFragment
import za.co.botcoin.wallet.fragments.wallet.menu.fragment.OrdersFrag
import za.co.botcoin.wallet.fragments.wallet.menu.fragment.ReceiveFrag
import za.co.botcoin.wallet.fragments.wallet.menu.fragment.SendFrag

class WalletMenuFrag : Fragment(R.layout.wallet_menu_fragment) {
    private lateinit var binding: WalletMenuFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = WalletMenuFragmentBinding.bind(view)

        addReceiveListener(view)
        addSendListener(view)
        addOrdersListener(view)
    }

    private fun addSendListener(view: View) {

        this.binding.linearLayoutSendOption.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("asset", arguments!!.getString("asset"))
            val sendFrag = SendFrag()
            sendFrag.arguments = bundle
            //startFragment((activity as MainActivity?)!!.supportFragmentManager, sendFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Send", true, false, true, null)
        }
    }

    private fun addReceiveListener(view: View) {
        this.binding.linearLayoutReceiveOption.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("asset", arguments!!.getString("asset"))
            val receiveFrag = ReceiveFrag()
            receiveFrag.arguments = bundle
            //startFragment((activity as MainActivity?)!!.supportFragmentManager, receiveFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Receive", true, false, true, null)
        }
    }

    private fun addOrdersListener(view: View) {
        this.binding.linearLayoutOrdersOption.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("asset", arguments!!.getString("asset"))
            val ordersFrag = OrdersFrag()
            ordersFrag.arguments = bundle
            //startFragment((activity as MainActivity?)!!.supportFragmentManager, ordersFrag, R.id.fragContainer, (activity as MainActivity?)!!.supportActionBar, "Orders", true, false, true, null)
        }
    }
}