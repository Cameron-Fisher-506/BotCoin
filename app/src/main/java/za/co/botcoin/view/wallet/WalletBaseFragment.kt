package za.co.botcoin.view.wallet

import android.content.Context
import androidx.fragment.app.Fragment

abstract class WalletBaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected lateinit var walletActivity: WalletActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        walletActivity = context as WalletActivity
    }
}