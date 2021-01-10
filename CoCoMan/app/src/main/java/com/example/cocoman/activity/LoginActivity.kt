package com.example.cocoman.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cocoman.R
import com.example.cocoman.data.LoginToken
import com.example.cocoman.network.MasterApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var userId :EditText
    lateinit var userPw: EditText
    lateinit var loginBtn: Button
    lateinit var registerBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView(this@LoginActivity)
        setupListener()

    }

    fun setupListener(){
        // 로그인 버튼 누른 경우
        loginBtn.setOnClickListener {
            val intent = Intent(this,InitialRatingActivity::class.java)
            tryLogin(intent)
        }
        // 회원가입 버튼 누른 경우
        registerBtn.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun initView(activity: Activity){
        userId = activity.findViewById(R.id.insert_id)
        userPw = activity.findViewById(R.id.insert_pw)
        loginBtn = activity.findViewById(R.id.login_btn)
        registerBtn = activity.findViewById(R.id.register_btn)
    }

    private fun tryLogin(intent: Intent){
        val userId = userId.text.toString()
        val userPw = userPw.text.toString()

        (application as MasterApplication).retrofitService.login(userId,userPw).enqueue(object: Callback<LoginToken>{
            override fun onFailure(call: Call<LoginToken>, t: Throwable) {
                Toast.makeText(this@LoginActivity,"서버와 통신 실패!",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoginToken>, response: Response<LoginToken>) {
                if(response.isSuccessful){
                    if(response.body()?.token!=null){
                        val token = response.body()!!.token
                        saveUserToken(token, this@LoginActivity)
                    }
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this@LoginActivity,"로그인 실패. \n아이디와 비밀번호를 확인한 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    //sharedPreference에 토큰 저장하는 함수
    fun saveUserToken(token:String, activity:Activity){
        val sp = activity.getSharedPreferences("login_sp",Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp",token)
        editor.commit()
    }
}



