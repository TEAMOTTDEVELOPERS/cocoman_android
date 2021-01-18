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
import androidx.core.content.ContextCompat
import com.example.cocoman.R
import com.example.cocoman.data.LoginToken
import com.example.cocoman.network.MasterApplication
import com.facebook.*

import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {


    lateinit var userId :EditText
    lateinit var userPw: EditText
    lateinit var loginBtn: Button
    lateinit var registerBtn : Button
    lateinit var facebookBtn :LoginButton
    lateinit var fbBtn:ImageButton
    lateinit var googleBtn :ImageButton
    lateinit var kakaoBtn :ImageButton
    lateinit var naverBtn :ImageButton
    lateinit var callbackManager:CallbackManager

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView(this@LoginActivity)
        setupListener()
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
        facebookInit()
        firebaseAuth = FirebaseAuth.getInstance()

    }


    fun setupListener(){
        // 로그인 버튼 누른 경우
        loginBtn.setOnClickListener {
            val userId = userId.text.toString()
            val intent = checkDoneInitialRating(userId)
            tryLogin(intent)
        }
        // 회원가입 버튼 누른 경우
        registerBtn.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        fbBtn.setOnClickListener{
            facebookBtn.performClick()
            if (isLoggedIn()) {
                val accessToken = AccessToken.getCurrentAccessToken()
                getFacebookInfo(accessToken)

            }else{
            }
        }

        googleBtn.setOnClickListener{
                googleSignIn()
        }
        kakaoBtn.setOnClickListener{
            tryKakaoLogin()

        }
        naverBtn.setOnClickListener{

        }
    }

    private fun googleSignIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount,data:String) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("msg","email:"+ it.result?.user?.email.toString())
                Log.d("msg", "token :" + data)
                checkIsRegisteredSocialLogin(it.result?.user?.email.toString(),data)

            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun initView(activity: Activity){
        userId = activity.findViewById(R.id.insert_id)
        userPw = activity.findViewById(R.id.insert_pw)
        loginBtn = activity.findViewById(R.id.login_btn)
        registerBtn = activity.findViewById(R.id.register_btn)
        facebookBtn = activity.findViewById(R.id.login_button_facebook)
        fbBtn = activity.findViewById(R.id.login_facebook)
        googleBtn = activity.findViewById(R.id.login_google)
        kakaoBtn = activity.findViewById(R.id.login_kakao)
        naverBtn = activity.findViewById(R.id.login_naver)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

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

    private fun tryKakaoLogin(){
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                   UserApiClient.instance.me { user, error ->
                       if (user != null) {
                           val kakaoUserEmail = user.kakaoAccount?.email?: "null"
                           Log.d("msg","user: "+ kakaoUserEmail)
                           Log.d("msg","token: "+ (token))
                           checkIsRegisteredSocialLogin(kakaoUserEmail,token.toString())
                       }
                   }
            }
        }

        if(LoginClient.instance.isKakaoTalkLoginAvailable(this)){
            LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
        }else{
            LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    fun facebookInit(){
        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this)
        callbackManager = CallbackManager.Factory.create()
        facebookBtn.setReadPermissions("email","public_profile")
        facebookBtn.registerCallback(callbackManager,object: FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                if(result?.accessToken!=null){
                    val accessToken = result.accessToken
                    getFacebookInfo(accessToken)

                }else{
                    Log.d("msg","access token is NULL")
                }
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
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

    // 소션 로그인 했을때 이미 유저가 가입되어있는지 확인후 어디로 보낼지 결정
    fun checkIsRegisteredSocialLogin(username:String, token:String){
        // TODO: 서버한테 유저 이멜 & 토큰 넘겨서 이미 가입된 유저 --> InitialRating, 아니면 RegisterForSocialLogin으로 넘겨주기!
        // 일단 true = 가입 되어있음 false = 가입 되어있지 않음
        var resultFromServer = false
        if(resultFromServer){
            val intent = Intent(this@LoginActivity, InitialRatingActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val Registerintent = Intent(this@LoginActivity, RegisterForSocialLoginActivity::class.java)
            Registerintent.putExtra("username",username)
            Registerintent.putExtra("token",token.toString())
            startActivity(Registerintent)
            finish()
        }
    }

    fun checkDoneInitialRating(username:String) : Intent{
        // TODO: 서버로부터 초기 콘텐츠 평가 했는지 가져오기
        var done = false
        if(done){
            return Intent(this@LoginActivity,MainActivity::class.java)
        }else{
            return Intent(this@LoginActivity,InitialRatingActivity::class.java)
        }

    }

    //accessToken으로 사용자 정보 가져오기 -- 페북
    private fun getFacebookInfo(accessToken : AccessToken){
        val graphrequest: GraphRequest = GraphRequest.newMeRequest(accessToken, object : GraphRequest.GraphJSONObjectCallback{
            override fun onCompleted(requestObject: JSONObject?, response: GraphResponse?) {
                if (response != null) {
                    if(response.error != null){

                    }else{
                        var facebook_useremail=""
                        try{
                            facebook_useremail = requestObject?.getString("email").toString()
                            Log.d("Result", ""+facebook_useremail);
                        }catch (e: JSONException){
                            e.printStackTrace()
                        }
                        val accessToken = AccessToken.getCurrentAccessToken()
                        Log.d("msg","email:"+facebook_useremail)
                        checkIsRegisteredSocialLogin(facebook_useremail,accessToken.toString())
                    }
                }
            }
        })

        var parameters : Bundle = Bundle()
        parameters.putString("fields","email")
        graphrequest.parameters = parameters
        graphrequest.executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { firebaseAuthWithGoogle(account!!, it) }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
                Log.d("msg",""+e.toString())
            }
        }
    }

    // 페북 로그인 되어있는지 확인
    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        return isLoggedIn
    }

}


