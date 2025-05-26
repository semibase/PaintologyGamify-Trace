package com.paintology.lite.trace.drawing.Activity.notifications.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.paintology.lite.trace.drawing.Activity.notifications.ui.FragmentAccountNotification
import com.paintology.lite.trace.drawing.Activity.notifications.ui.FragmentSystemNotification

class NotificationViewPager(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // Number of fragments
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentAccountNotification()
            1 -> FragmentSystemNotification()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

}
