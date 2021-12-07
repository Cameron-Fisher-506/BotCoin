package za.co.botcoin.view.home

import android.content.Context
import androidx.fragment.app.Fragment

abstract class HomeBaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected lateinit var homeActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as MainActivity
    }
}