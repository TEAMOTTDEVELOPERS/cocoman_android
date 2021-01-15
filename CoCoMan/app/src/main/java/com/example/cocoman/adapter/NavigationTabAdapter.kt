package com.example.cocoman.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.cocoman.fragment.*


class NavigationTabAdapter (fm:FragmentManager,val fragmentCount:Int, val userId: Int): FragmentStatePagerAdapter(fm){
    override fun getCount(): Int {
        return fragmentCount
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment.newInstance(userId)
            1 -> return SearchFragment.newInstance(userId)
            2 -> return OTTRecommendFragment.newInstance(userId)
            3 -> return GroupFragment.newInstance(userId)
            else -> return MyProfileFragment.newInstance(userId)
        }
    }
}
