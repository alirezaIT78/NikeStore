package com.example.nikeprojectfinaltest2.feucher.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nikeprojectfinaltest2.data.Banner

class viewPagerAdapter(fragment: Fragment,val banners:List<Banner>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
       return banners.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragmentBanner=FragmentBanner()
       return fragmentBanner.newInstance(banners.get(position))
    }
}