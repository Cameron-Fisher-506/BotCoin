package za.co.botcoin.view.home

import android.content.Context
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

abstract class HomeBaseFragment(layoutId: Int = -1) : Fragment(layoutId) {
    protected lateinit var homeActivity: MainActivity
    protected lateinit var menuHost: MenuHost
    protected val homeViewModel by activityViewModels<HomeViewModel>(factoryProducer = { homeActivity.getViewModelFactory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as MainActivity
        menuHost = context
    }
}