package za.co.ticker.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import za.co.ticker.R
import za.co.ticker.databinding.NotificationsFragmentBinding

class NotificationsFragment : Fragment(R.layout.notifications_fragment) {
    private lateinit var binding: NotificationsFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = NotificationsFragmentBinding.bind(view)
    }
}