package com.example.cocoman.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.example.cocoman.R
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    var userId: Int=1
    private lateinit var fragmentPage:ViewPager
    private lateinit var navBar :TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeBottomNavigation()
        fragmentPage = findViewById(R.id.fragment_page)
        navBar =findViewById(R.id.nav_bar)
        fragmentPage.currentItem = 0

    }
    private fun makeBottomNavigation(){
        val pagerAdapter = NavigationTabAdapter(
            supportFragmentManager,
            5
        )
        fragmentPage.adapter = pagerAdapter
        navBar.setupWithViewPager(fragmentPage)

        val navigationLayout: View = this.layoutInflater.inflate(R.layout.navigation_tab,null,false)
        navBar.getTabAt(0)!!.customView=navigationLayout.findViewById(R.id.home_tab) as RelativeLayout
        navBar.getTabAt(1)!!.customView=navigationLayout.findViewById(R.id.search_tab) as RelativeLayout
        navBar.getTabAt(2)!!.customView=navigationLayout.findViewById(R.id.recommend_tab) as RelativeLayout
        navBar.getTabAt(3)!!.customView=navigationLayout.findViewById(R.id.group_tab) as RelativeLayout
        navBar.getTabAt(4)!!.customView=navigationLayout.findViewById(R.id.profile_tab) as RelativeLayout
    }

}