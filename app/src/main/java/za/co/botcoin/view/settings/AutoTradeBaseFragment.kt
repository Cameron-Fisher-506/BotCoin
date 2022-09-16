package za.co.botcoin.view.settings

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

abstract class AutoTradeBaseFragment(layoutId: Int): Fragment(layoutId) {
    protected lateinit var autoTradeActivity: AutoTradeActivity
    protected val autoTradeViewModel by activityViewModels<AutoTradeViewModel>(factoryProducer = { autoTradeActivity.getViewModelFactory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        autoTradeActivity = context as AutoTradeActivity
    }
}