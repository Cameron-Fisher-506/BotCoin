package za.co.botcoin.view.menu

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

abstract class MenuBaseFragment(layoutId: Int = -1) : Fragment(layoutId) {
    protected lateinit var menuActivity: MenuActivity
    protected val menuViewModel by activityViewModels<MenuViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuActivity = context as MenuActivity
    }
}