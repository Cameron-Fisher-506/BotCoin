package za.co.botcoin.view.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import za.co.botcoin.R
import za.co.botcoin.databinding.NotificationsFragmentBinding

class NotificationsFrag : Fragment(R.layout.notifications_fragment) {
    private lateinit var binding: NotificationsFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = NotificationsFragmentBinding.bind(view)
    }
}