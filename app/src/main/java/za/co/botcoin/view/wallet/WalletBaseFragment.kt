package za.co.botcoin.view.wallet

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

abstract class WalletBaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected lateinit var walletActivity: WalletActivity
    protected val walletViewModel by activityViewModels<WalletViewModel>(factoryProducer = { walletActivity.getViewModelFactory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        walletActivity = context as WalletActivity
    }
}