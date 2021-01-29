package com.example.cocoman.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocoman.R
import com.example.cocoman.`interface`.onContentRatingStatusChangeListener
import com.example.cocoman.adapter.ContentRatingAdaptor
import com.example.cocoman.data.ContentRating
import com.scwang.wave.MultiWaveHeader

class InitialRatingActivity : AppCompatActivity() {
    lateinit var rateCount : TextView
    lateinit var wave : MultiWaveHeader
    lateinit var doneBtn: Button
    lateinit var userNickname : TextView
    lateinit var progressbar:ProgressBar
    lateinit var contentRecyclerView: RecyclerView
    var rateCompleted:Int = 0
    var shouldBeRated : Int = 10-rateCompleted
    var contentList = ArrayList<ContentRating>()
    var page = 1
    var limit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        // 맨 처음 뷰 만들때
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_rating_acitivity)
        initView()
        setupListener()
        getData()
        layoutInit()
        wavesettings()
        userNickname.setText("지수")

    }

    fun getData(){
        // TODO: 서버로부터 데이터 가져오기 -- pagination은 서버가 완료되야 테스트 해보면서 할수 있을거같아요!
        for (i in 0 until 10) {
            contentList.add(ContentRating("Harry Potter 2", 2012, "영화", "판타지",0.0F))
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
        contentRecyclerView.adapter=adapter
        contentRecyclerView.layoutManager = LinearLayoutManager(this@InitialRatingActivity)
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
        contentRecyclerView = findViewById(R.id.content_recylerview)
        userNickname = findViewById(R.id.rating_userNickName)
        rateCount = findViewById(R.id.progress_bar_count)
        doneBtn = findViewById(R.id.ok_rating)
        wave=findViewById(R.id.waveHeader)
        progressbar = findViewById(R.id.progress_bar_rating)
    }

    fun wavesettings(){
        wave.velocity= 5F
        wave.setProgress(1F)
        wave.isRunning
        wave.gradientAngle = 95
        wave.waveHeight=40
    }

    fun incrementRated(position: Int, score: Float){
        rateCompleted+=1
        shouldBeRated = 10-rateCompleted
        contentList[position].score = score
        Log.d("rate scor (INCREMENT)",""+contentList[position].score)
        if(rateCompleted>=10){
            rateCount.setText("10개 달성!")
            progressbar.progress=10
        }else{
            rateCount.setText(""+shouldBeRated+"개 남음")
            progressbar.progress=rateCompleted
        }
    }

    fun decrementRated(position: Int){
        rateCompleted-=1
        shouldBeRated = 10-rateCompleted
        contentList[position].score = 0.0F
        if(rateCompleted>=10){
            rateCount.setText("10개 달성!")
            progressbar.progress=10
        }else{
            rateCount.setText(""+shouldBeRated+"개 남음")
            progressbar.progress=rateCompleted
        }
    }
}