package com.example.cocoman.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocoman.R
import com.example.cocoman.`interface`.onContentRatingStatusChangeListener
import com.example.cocoman.adapter.ContentRatingAdaptor
import com.example.cocoman.data.ContentRating
import kotlinx.android.synthetic.main.activity_initial_rating_acitivity.*
import kotlinx.android.synthetic.main.rating_content_view.*

class InitialRatingActivity : AppCompatActivity() {
    lateinit var totalRated : TextView
    lateinit var rateComment: TextView
    lateinit var doneBtn: Button
    var rateCompleted:Int = 0
    var shouldBeRated : Int = 10-rateCompleted
    var contentList = ArrayList<ContentRating>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 맨 처음 뷰 만들때
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_rating_acitivity)
        initView()
        setupListener()
        getData()
        layoutInit()



    }

    fun getData(){
        // TODO: 서버로부터 데이터 가져오기
        for (i in 0 until 10) {
            contentList.add(ContentRating("Harry Potter 2", 2012, 0.0F))
        }
    }
    fun layoutInit(){
        val adapter = ContentRatingAdaptor(contentList, LayoutInflater.from(this@InitialRatingActivity),object : onContentRatingStatusChangeListener{
            override fun onContentRated(position: Int,p1:Float) {
                incrementRated(position,p1)

            }

            override fun onContentUnrated(position: Int,p1:Float) {
               decrementRated(position)
            }
        })
        content_recylerview.adapter=adapter
        content_recylerview.layoutManager = LinearLayoutManager(this@InitialRatingActivity)
    }
    fun setupListener(){
        doneBtn.setOnClickListener {
            if(shouldBeRated<=0) {
                // TODO: 사용자가 이미 초기 콘텐츠 평가 했다고 서버에 알려주기
                val intent = Intent(this@InitialRatingActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this@InitialRatingActivity,""+shouldBeRated+"개를 더 평가해주세요.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun initView(){
        // lateinit 으로 만든거 xml 에서 요소 찾아주기
        totalRated = findViewById(R.id.rating_totalNum)
        rateComment = findViewById(R.id.rating_ment)
        doneBtn = findViewById(R.id.ok_rating)
    }


    fun incrementRated(position: Int, score: Float){
        rateCompleted+=1
        shouldBeRated = 10-rateCompleted
        contentList[position].score = score
        Log.d("rate scor (INCREMENT)",""+contentList[position].score)
        totalRated.setText(rateCompleted.toString())
        if(rateCompleted>=10){
            rateComment.setText("10개 달성!")
        }else{
            rateComment.setText(""+shouldBeRated+"개를 더 평가해주세요.")
        }
    }

    fun decrementRated(position: Int){
        rateCompleted-=1
        shouldBeRated = 10-rateCompleted
        contentList[position].score = 0.0F
        totalRated.setText(rateCompleted.toString())
        Log.d("rate score",""+contentList[position].score)
        if(rateCompleted>=10){
            rateComment.setText("10개 달성!")
        }else{
            rateComment.setText(""+shouldBeRated+"개를 더 평가해주세요.")
        }
    }
}