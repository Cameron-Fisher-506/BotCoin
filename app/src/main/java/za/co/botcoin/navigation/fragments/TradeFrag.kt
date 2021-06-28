package za.co.botcoin.navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import za.co.botcoin.R
import za.co.botcoin.databinding.TradeOptionFragmentBinding
import za.co.botcoin.objs.Trade
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.viewpageradapters.TradeAdapter
import java.util.*

class TradeFrag : Fragment(R.layout.trade_option_fragment) {
    private lateinit var binding: TradeOptionFragmentBinding

    private var trades: ArrayList<Trade> = ArrayList()
    private var tradeAdapter: TradeAdapter? = null

    companion object {
        const val TITLE = "Trading Bot"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = TradeOptionFragmentBinding.bind(view)

        trades.add(Trade(Trade.BUY_TYPE, "0", ConstantUtils.SUPPORT_PRICE ?: ""))
        trades.add(Trade(Trade.SELL_TYPE, "0", ConstantUtils.RESISTANCE_PRICE ?: ""))
        tradeAdapter = TradeAdapter(context, trades)
        this.binding.botPager.adapter = tradeAdapter
        addTabBotMenuListener()
    }

    private fun addTabBotMenuListener() {
        this.binding.tabBotMenu.addOnTabSelectedListener(object : OnTabSelectedListener {
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