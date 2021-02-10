package com.example.cocoman.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cocoman.R
import com.example.cocoman.data.Register
import com.example.cocoman.login.LoginActivity

class SelectOTTActivity : AppCompatActivity() {
    lateinit var netflixBtn:TextView
    lateinit var watchaBtn:TextView
    lateinit var tvingBtn: TextView
    lateinit var naverseriesBtn:TextView
    lateinit var wavveBtn:TextView
    lateinit var kakaotvBtn:TextView
    lateinit var seeznBtn:TextView
    lateinit var coupangBtn:TextView
    lateinit var createAccountBtn:Button
    lateinit var gobackBtn:ImageView
    var ottContents=ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_o_t_t)
        initView()
        setupListener()
        val username: String? = intent.getStringExtra("username")
        val userPassword:String? = intent.getStringExtra("userPassword")
        val userGender:String? = intent.getStringExtra("userGender")
        val userAge:String? = intent.getStringExtra("userAge")
        Log.d("msg","username:"+username)
        Log.d("msg","userpw:"+userPassword)
        Log.d("msg","userage:"+userAge)
        Log.d("msg","usergender:"+userGender)
    }

    fun initView(){
        netflixBtn=findViewById(R.id.netflix_selectott)
        watchaBtn=findViewById(R.id.watcha_selectott)
        tvingBtn = findViewById(R.id.tving_selectott)
        naverseriesBtn=findViewById(R.id.naver_selectott)
        wavveBtn=findViewById(R.id.wavve_selectott)
        kakaotvBtn=findViewById(R.id.kakao_selectott)
        seeznBtn=findViewById(R.id.seezn_selectott)
        coupangBtn=findViewById(R.id.coupang_selectott)
        createAccountBtn=findViewById(R.id.make_account_selectott)
        gobackBtn=findViewById(R.id.gobackBtn_selectOTT_register)
    }

    fun setupListener(){
        createAccountBtn.setOnClickListener {
            //TODO: 서버와 연결 (회원가입)
            val intent = Intent(this@SelectOTTActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        gobackBtn.setOnClickListener {
            val intent = Intent(this@SelectOTTActivity,Register::class.java)
            startActivity(intent)
            finish()
        }
        selectOTT(netflixBtn)
        selectOTT(watchaBtn)
        selectOTT(tvingBtn)
        selectOTT(naverseriesBtn)
        selectOTT(kakaotvBtn)
        selectOTT(wavveBtn)
        selectOTT(seeznBtn)
        selectOTT(coupangBtn)
    }

    // 선택/해제하면 UI 바뀌고, ottContents에 저장
    fun selectOTT(textView: TextView){
        textView.setOnClickListener {
            var ottSelected = textView.text.split("Btn")
            var selectedViewName = ""+ottSelected[0].toLowerCase().replace("\\s+".toRegex(), "")+"_selected"
            var selectedView:ConstraintLayout = findViewById(resources.getIdentifier(selectedViewName,"id","com.example.cocoman"))
            Log.d("name: ",""+selectedViewName)
            Log.d("ott: ",""+ottSelected[0])
            if(ottContents.contains(ottSelected[0])){
                selectedView.elevation =0F
                Log.d("contents","removed" )
                Log.d("contents",""+ottContents)
                ottContents.remove(ottSelected[0])
            }else{
                selectedView.elevation =10F
               ottContents.add(ottSelected[0])
                Log.d("contents",""+ottContents)
            }
        }

    }
}