package com.example.cocoman.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.example.cocoman.R
import com.example.cocoman.adapter.NavigationTabAdapter
import com.google.android.material.tabs.TabLayout
//import com.example.cocoman.adapter.NavigationTabAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        nav_bar.addTab(nav_bar.newTab().setText("홈"))
//        nav_bar.addTab(nav_bar.newTab().setText("검색"))
//        nav_bar.addTab(nav_bar.newTab().setText("추천"))
//        nav_bar.addTab(nav_bar.newTab().setText("그룹"))
//        nav_bar.addTab(nav_bar.newTab().setText("프로필"))
        makeBottomNavigation()
        fragment_page.setCurrentItem(0)

    }
    private fun makeBottomNavigation(){
        val pagerAdapter = NavigationTabAdapter(supportFragmentManager,5)
        fragment_page.adapter = pagerAdapter
        nav_bar.setupWithViewPager(fragment_page)

        val navigationLayout: View = this.layoutInflater.inflate(R.layout.navigation_tab,null,false)
        nav_bar.getTabAt(0)!!.customView=navigationLayout.findViewById(R.id.home_tab) as RelativeLayout
        nav_bar.getTabAt(1)!!.customView=navigationLayout.findViewById(R.id.search_tab) as RelativeLayout
        nav_bar.getTabAt(2)!!.customView=navigationLayout.findViewById(R.id.recommend_tab) as RelativeLayout
        nav_bar.getTabAt(3)!!.customView=navigationLayout.findViewById(R.id.group_tab) as RelativeLayout
        nav_bar.getTabAt(4)!!.customView=navigationLayout.findViewById(R.id.profile_tab) as RelativeLayout
    }

}