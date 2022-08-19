package za.co.botcoin.view.menu

import android.content.Context
import androidx.fragment.app.Fragment

abstract class MenuBaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected lateinit var menuActivity: MenuActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuActivity = context as MenuActivity
    }
}