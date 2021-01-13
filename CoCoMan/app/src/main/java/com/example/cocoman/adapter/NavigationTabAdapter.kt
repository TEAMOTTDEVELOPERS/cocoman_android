package com.example.cocoman.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.cocoman.fragment.*

class NavigationTabAdapter (fm:FragmentManager,val fragmentCount:Int): FragmentStatePagerAdapter(fm){
    override fun getCount(): Int {
        return fragmentCount
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment()
            1 -> return SearchFragment()
            2 -> return OTTRecommendFragment()
            3 -> return GroupFragment()
            else -> return MyProfileFragment()
        }
    }
}