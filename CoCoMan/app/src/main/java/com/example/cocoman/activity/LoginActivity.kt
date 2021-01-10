package com.example.cocoman.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cocoman.R
import com.example.cocoman.data.LoginToken
import com.example.cocoman.network.MasterApplication
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.LoginButton
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    lateinit var userId :EditText
    lateinit var userPw: EditText
    lateinit var loginBtn: Button
    lateinit var registerBtn : Button
    lateinit var facebookBtn :ImageButton
    lateinit var googleBtn :ImageButton
    lateinit var kakaoBtn :LoginButton
    lateinit var naverBtn :ImageButton

    private var callback: SessionCallback = SessionCallback()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView(this@LoginActivity)
        setupListener()
        getAppKeyHash()
        Session.getCurrentSession().addCallback(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.d("msg","session get current session")
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getAppKeyHash() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString())
        }
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

        facebookBtn.setOnClickListener{

        }

        googleBtn.setOnClickListener{

        }
        kakaoBtn.setOnClickListener{
            Session.getCurrentSession().addCallback(callback)
            Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
        }
        naverBtn.setOnClickListener{

        }
    }

    fun initView(activity: Activity){
        userId = activity.findViewById(R.id.insert_id)
        userPw = activity.findViewById(R.id.insert_pw)
        loginBtn = activity.findViewById(R.id.login_btn)
        registerBtn = activity.findViewById(R.id.register_btn)
        facebookBtn = activity.findViewById(R.id.login_facebook)
        googleBtn = activity.findViewById(R.id.login_google)
        kakaoBtn = activity.findViewById(R.id.login_kakao)
        naverBtn = activity.findViewById(R.id.login_naver)
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
private class SessionCallback : ISessionCallback {
    override fun onSessionOpenFailed(exception: KakaoException?) {
        Log.e("msg","Session Call back :: onSessionOpenFailed: ${exception?.message}")
    }

    override fun onSessionOpened() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {

            override fun onFailure(errorResult: ErrorResult?) {
                Log.d("msg","Session Call back :: on failed ${errorResult?.errorMessage}")
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Log.e("msg","Session Call back :: onSessionClosed ${errorResult?.errorMessage}")

            }

            override fun onSuccess(result: MeV2Response?) {
                checkNotNull(result) { "session response null" }
                // register or login
            }

        })
    }

}

