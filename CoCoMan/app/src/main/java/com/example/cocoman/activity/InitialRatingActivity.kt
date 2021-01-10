package com.example.cocoman.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AbsListView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocoman.R
import com.example.cocoman.adapter.ContentRatingAdaptor
import com.example.cocoman.data.ContentRating
import kotlinx.android.synthetic.main.activity_initial_rating_acitivity.*

class InitialRatingActivity : AppCompatActivity() {
    lateinit var totalRated : TextView
    lateinit var rateComment: TextView
    lateinit var doneBtn: Button
    var rateCompleted:Int = 0
    var shouldBeRated : Int = 10-rateCompleted

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_rating_acitivity)
        initView()
        doneBtn.setOnClickListener {
            val intent = Intent(this@InitialRatingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        layoutInit()

    }
    fun layoutInit(){
        val contentList = ArrayList<ContentRating>()
        for ( i in 0 until 10){
            contentList.add(ContentRating("Harry Potter 2",2012,1))
        }
        val adapter = ContentRatingAdaptor(contentList, LayoutInflater.from(this@InitialRatingActivity))
        content_recylerview.adapter=adapter
        content_recylerview.layoutManager = LinearLayoutManager(this@InitialRatingActivity)
    }

    fun initView(){
        totalRated = findViewById(R.id.rating_totalNum)
        rateComment = findViewById(R.id.rating_ment)
        doneBtn = findViewById(R.id.ok_rating)
    }

    fun incrementRated(){
        rateCompleted+=1
        totalRated.setText(rateCompleted.toString())
        if(rateCompleted>=10){
            rateComment.setText("10개 달성!")
        }else{
            rateComment.setText(""+shouldBeRated+"개를 더 평가해주세요.")
        }
    }

    fun decrementRated(){
        rateCompleted-=1
        totalRated.setText(rateCompleted.toString())
        if(rateCompleted>=10){
            rateComment.setText("10개 달성!")
        }else{
            rateComment.setText(""+shouldBeRated+"개를 더 평가해주세요.")
        }
    }
}