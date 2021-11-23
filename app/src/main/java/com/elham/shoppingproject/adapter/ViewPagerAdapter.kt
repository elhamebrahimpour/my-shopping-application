package com.elham.shoppingproject.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elham.shoppingproject.views.BasketFragment
import com.elham.shoppingproject.views.HomeFragment
import com.elham.shoppingproject.views.LoginFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment()
            1 -> return BasketFragment()
            2 -> return LoginFragment()
        }
        return HomeFragment()
    }
}