package com.example.imageshering.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imageshering.ui.main.collections.CollectionsHostFragment
import com.example.imageshering.ui.main.home.HomeFragment
import com.example.imageshering.ui.main.profile.ProfileFragment

class PagerAdapter(
    fragmentActivity: FragmentActivity,
    var homeFragment: HomeFragment? = null
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val t1 = when (position) {
            0 -> HomeFragment().apply { homeFragment = this }
            1 -> CollectionsHostFragment()
            else -> ProfileFragment()
        }
        return t1
    }
}