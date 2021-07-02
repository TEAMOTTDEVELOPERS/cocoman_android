package com.example.cocoman.main

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.example.cocoman.BaseActivity
import com.example.cocoman.R
import com.google.android.material.tabs.TabLayout

class MainActivity : BaseActivity() {
    var userId: Int=1
    private lateinit var fragmentPage:ViewPager
    private lateinit var navBar :TabLayout
    private lateinit var homeTabImg:ImageView
    private lateinit var homeTabText:TextView
    private lateinit var searchTabImg:ImageView
    private lateinit var searchTabText:TextView
    private lateinit var groupTabImg:ImageView
    private lateinit var groupTabText:TextView
    private lateinit var rateTabImg:ImageView
    private lateinit var rateTabText:TextView
    private lateinit var profileTabImg:ImageView
    private lateinit var profileTabText:TextView
    private lateinit var navigationLayout:View

    val selectedImage = mapOf<Int,Int>(
    0 to R.drawable.home_icon_selected,
    1 to R.drawable.search_icon_selected,
    2 to R.drawable.group_icon_selected,
    3 to R.drawable.rate_icon_selected,
    4 to R.drawable.profile_icon_selected
    )

    val unselectedImage = mapOf<Int,Int>(
        0 to R.drawable.home_icon_unselected,
        1 to R.drawable.search_icon_unselected,
        2 to R.drawable.group_icon_unselected,
        3 to R.drawable.rate_icon_unselected,
        4 to R.drawable.profile_icon_unselected
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        makeBottomNavigation()

    }
    private fun initView(){
        fragmentPage = findViewById(R.id.fragment_page)
        navBar =findViewById(R.id.nav_bar)
        val pagerAdapter = NavigationTabAdapter(
            supportFragmentManager,
            5
        )
        fragmentPage.adapter = pagerAdapter
        navBar.setupWithViewPager(fragmentPage)
        navigationLayout= this.layoutInflater.inflate(R.layout.navigation_tab,null,false)

        homeTabImg = navigationLayout.findViewById(R.id.home_icon)
        homeTabText = navigationLayout.findViewById(R.id.home_text)
        searchTabImg = navigationLayout.findViewById(R.id.search_icon)
        searchTabText = navigationLayout.findViewById(R.id.search_text)
        groupTabImg = navigationLayout.findViewById(R.id.group_icon)
        groupTabText = navigationLayout.findViewById(R.id.group_text)
        rateTabImg = navigationLayout.findViewById(R.id.rate_icon)
        rateTabText = navigationLayout.findViewById(R.id.rate_text)
        profileTabImg = navigationLayout.findViewById(R.id.profile_icon)
        profileTabText = navigationLayout.findViewById(R.id.profile_text)

        fragmentPage.currentItem = 0
        selectTab(0,1)

    }

    private fun makeBottomNavigation(){
        navBar.getTabAt(0)!!.customView=navigationLayout.findViewById(R.id.home_tab) as ConstraintLayout
        navBar.getTabAt(1)!!.customView=navigationLayout.findViewById(R.id.search_tab) as ConstraintLayout
        navBar.getTabAt(2)!!.customView=navigationLayout.findViewById(R.id.group_tab) as ConstraintLayout
        navBar.getTabAt(3)!!.customView=navigationLayout.findViewById(R.id.rate_tab) as ConstraintLayout
        navBar.getTabAt(4)!!.customView=navigationLayout.findViewById(R.id.profile_tab) as ConstraintLayout

        navBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    selectTab(tab.position,1)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    selectTab(tab.position,0)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                selectTab(tab.position,1)
            }
        })

    }

    private fun selectTab(index:Int,flag:Int){
        val tabImageView = mapOf<Int, ImageView>(
            0 to homeTabImg,
            1 to searchTabImg,
            2 to groupTabImg,
            3 to rateTabImg,
            4 to profileTabImg
        )

        val tabTextView = mapOf<Int, TextView>(
            0 to homeTabText,
            1 to searchTabText,
            2 to groupTabText,
            3 to rateTabText,
            4 to profileTabText
        )

        val typeFace = if (flag==1) Typeface.BOLD else Typeface.NORMAL
        val textColor = Color.parseColor(if (flag==1) "#191919" else "#999999")

        if(flag==1) {
            selectedImage[index]?.let { tabImageView[index]?.setImageResource(it) }
        }else{
            unselectedImage[index]?.let { tabImageView[index]?.setImageResource(it) }
        }

        tabTextView[index]?.setTextColor(textColor)
        tabTextView[index]?.setTypeface(null,typeFace)

    }

}