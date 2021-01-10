package com.example.cocoman.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.example.cocoman.R
//import com.example.cocoman.adapter.NavigationTabAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
//
//    private fun bottomNavigation(){
//        fragment_page.adapter = NavigationTabAdapter(supportFragmentManager,5)
//        nav_bar.setupWithViewPager(fragment_page)
//
//        val bottomTab : View = this.layoutInflater.inflate(R.layout.navigation_tab,null,false)
//
//        nav_bar.getTabAt(0)!!.customView = bottomTab.findViewById(R.id.home_tab) as RelativeLayout
//    }
}