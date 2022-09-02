package za.co.botcoin.view.settings

import android.content.Context
import androidx.fragment.app.Fragment

abstract class AutoTradeBaseFragment(layoutId: Int): Fragment(layoutId) {
    protected lateinit var autoTradeActivity: AutoTradeActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        autoTradeActivity = context as AutoTradeActivity
    }
}