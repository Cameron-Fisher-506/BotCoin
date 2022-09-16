package za.co.botcoin.view.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

abstract class HomeBaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected lateinit var homeActivity: MainActivity
    protected val homeViewModel by activityViewModels<HomeViewModel>(factoryProducer = { homeActivity.getViewModelFactory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as MainActivity
    }
}