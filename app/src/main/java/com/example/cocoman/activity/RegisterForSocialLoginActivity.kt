package com.example.cocoman.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.example.cocoman.R
import com.example.cocoman.initrate.InitialRatingActivity

class RegisterForSocialLoginActivity : AppCompatActivity() {
    lateinit var userAge: EditText
    lateinit var createAccountBtn : Button
    lateinit var username:TextView
    var ottContents=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        var name = getIntent().getStringExtra("username")
        var token_ = getIntent().getStringExtra("token")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_for_social_login)
        initView(this@RegisterForSocialLoginActivity)
        username.setText(name).toString()
        createAccountBtn.setOnClickListener {
            register()
        }
    }

    fun initView(activity: Activity){
        username=activity.findViewById(R.id.username_sl)
        userAge = activity.findViewById(R.id.age_register_sl)
        createAccountBtn = activity.findViewById(R.id.make_account_sl)
    }


    fun register(){
        // TODO: register 서버랑 연결
        //register(user,token_,getUserGender(),getUserAge(),ottContents)
        val intent = Intent(this, InitialRatingActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onCheckboxClicked(view: View) {
        if(view is CheckBox){
            val checked : Boolean = view.isChecked
            when(view.id){
                R.id.netflix_sl -> {
                    if(checked){
                        ottContents.add("Netflix")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Netflix")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.watcha_sl -> {
                    if(checked){
                        ottContents.add("Watcha")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Watcha")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.tving_sl -> {
                    if(checked){
                        ottContents.add("Tving")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Tving")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.naver_sl -> {
                    if(checked){
                        ottContents.add("Naver")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Naver")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.kakaoTv_sl -> {
                    if(checked){
                        ottContents.add("Kakao Tv")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Kakao Tv")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.wavve_sl -> {
                    if(checked){
                        ottContents.add("Wavve")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Wavve")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.seezen_sl -> {
                    if(checked){
                        ottContents.add("Seezen")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Seezen")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.coupangPlay_sl -> {
                    if(checked){
                        ottContents.add("Coupang Play")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Coupang Play")
                        Log.d("contents",""+ottContents)
                    }
                }
            }
        }
    }

}