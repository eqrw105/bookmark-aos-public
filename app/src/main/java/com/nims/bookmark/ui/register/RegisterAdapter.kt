package com.nims.bookmark.ui.register

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RegisterAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    var items: List<Fragment> = listOf()
    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }
}