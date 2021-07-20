package za.co.ticker.view.trade

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import za.co.ticker.R
import za.co.ticker.databinding.TradeOptionFragmentBinding
import za.co.ticker.model.models.Trade
import za.co.ticker.utils.ConstantUtils
import java.util.*

class TradeFragment : Fragment(R.layout.trade_option_fragment) {
    private lateinit var binding: TradeOptionFragmentBinding

    private var trades: ArrayList<Trade> = ArrayList()
    private var tradeAdapter: TradeAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = TradeOptionFragmentBinding.bind(view)

        trades.add(Trade(Trade.BUY_TYPE, "0", ConstantUtils.SUPPORT_PRICE ?: ""))
        trades.add(Trade(Trade.SELL_TYPE, "0", ConstantUtils.RESISTANCE_PRICE ?: ""))
        tradeAdapter = TradeAdapter(context!!, trades)
        this.binding.botPager.adapter = tradeAdapter
        addTabBotMenuListener()
    }

    private fun addTabBotMenuListener() {
        this.binding.tabBotTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 ->                         //BUY
                        binding.botPager.currentItem = 0
                    1 ->                         //SELL
                        binding.botPager.currentItem = 1
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}