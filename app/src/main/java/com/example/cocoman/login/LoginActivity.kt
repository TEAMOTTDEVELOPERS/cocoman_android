package com.example.cocoman.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import com.example.cocoman.BaseActivity
import com.example.cocoman.R
import com.example.cocoman.activity.RegisterActivity
import com.example.cocoman.data.source.request.LoginRequest
import com.example.cocoman.initrate.InitialRatingActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity(), LoginContract.View {
    @Inject
    lateinit var presenter: LoginContract.Presenter
    private lateinit var userId: EditText 
    private lateinit var userPw: EditText
    lateinit var loginBtn: Button
    lateinit var registerBtn : TextView
    lateinit var findPwBtn : TextView
    lateinit var findIDBtn: TextView
    lateinit var deleteIDBtn:ImageView
    lateinit var deletePwBtn:ImageView
    lateinit var personIcon : ImageView
    lateinit var lockIcon : ImageView
    lateinit var errorMsg : TextView
    lateinit var autoLoginBtn:ImageView
    lateinit var saveIdBtn: ImageView
    lateinit var facebookBtn : LoginButton
    lateinit var fbBtn:ImageView
    lateinit var googleBtn :ImageView
    lateinit var kakaoBtn :ImageView
    lateinit var naverBtn : OAuthLoginButton
    // rememberChecked --> 0 = none, 1 = autologin, 2 = saveID
    var rememberChecked:Int = 0
    val RC_SIGN_IN: Int = 1
    lateinit var callbackManager:CallbackManager
    lateinit var mContext: Context
    lateinit var mOAuthLoginModule : OAuthLogin
    lateinit var  mOAuthLoginHandler: OAuthLoginHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initNaverHandler()
        initNaver()
        initView()
        setupListener()
    }

    private fun initView() {
        presenter.attach(this)
        userId = findViewById(R.id.insert_id)
        userPw = findViewById(R.id.insert_pw)
        loginBtn = findViewById(R.id.login_btn)
        registerBtn = findViewById(R.id.register_btn)
        findIDBtn = findViewById(R.id.lost_id)
        findPwBtn = findViewById(R.id.lost_pw)
        deleteIDBtn = findViewById(R.id.delete_id_login)
        deletePwBtn = findViewById(R.id.delete_pw_login)
        facebookBtn = findViewById(R.id.login_button_facebook)
        fbBtn = findViewById(R.id.login_facebook)
        googleBtn = findViewById(R.id.login_google)
        kakaoBtn = findViewById(R.id.login_kakao)
        naverBtn = findViewById(R.id.login_naver)
        naverBtn.setOAuthLoginHandler(mOAuthLoginHandler)
        personIcon = findViewById(R.id.insert_id_personicon)
        lockIcon = findViewById(R.id.insert_pw_lockicon)
        errorMsg = findViewById(R.id.error_msg_login)
        autoLoginBtn = findViewById(R.id.autologinBtn_login)
        saveIdBtn = findViewById(R.id.saveIdBtn_login)
    }

    private fun setupListener(){
        loginBtn.setOnClickListener {
                presenter.tryLogin(
                    userId.text.toString(),
                    userPw.text.toString()
                )
        }


        registerBtn.setOnClickListener {
            navigateWithFinish(RegisterActivity::class)
        }

        kakaoBtn.setOnClickListener {
            presenter.tryKakaoLogin(this@LoginActivity)
        }

        googleBtn.setOnClickListener {
            presenter.tryGoogleLoginInit(this@LoginActivity)
        }

        facebookBtn.apply{
            facebookBtn.setReadPermissions("email","public_profile")
            setOnClickListener {
                presenter.tryFacebookLogin(
                    this@LoginActivity,
                    facebookBtn)
            }
        }
        findIDBtn.setOnClickListener{

        }
        findPwBtn.setOnClickListener{

        }
        // 사람 이미지 바꾸기
        userId.setOnFocusChangeListener{ _, hasFocus ->
            if (hasFocus)
                personIcon.setImageResource(R.drawable.insertidaf)
            else
                personIcon.setImageResource(R.drawable.insertidbf)
        }

        userPw.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
            // TODO: 비번으로 왔으면 자동으로 위에 적은 아이디가 어플에 등록되어있는지 check!
                lockIcon.setImageResource(R.drawable.insertpwaf)
            else
                lockIcon.setImageResource(R.drawable.insertpwbf)
        }

        //edittext 필드에 text 있으면 x 버튼 표시, 없으면 x 버튼도 없애기
        showDeleteButton(userPw,deletePwBtn)
        showDeleteButton(userId,deleteIDBtn)
        deletePwBtn.setOnClickListener {
            userPw.setText("")
        }

        deleteIDBtn.setOnClickListener {
                userId.setText("")
        }


        saveIdBtn.setOnClickListener {
            // 원래 체크 되어있었는데 한번더 누름 --> uncheck
            if (rememberChecked == 2) {
                rememberChecked = 0
                changeRadioStatus(rememberChecked)
            } else {
                rememberChecked = 2
                changeRadioStatus(rememberChecked)
            }
        }
        autoLoginBtn.setOnClickListener {
            // 원래 체크 되어있었는데 한번더 누름 --> uncheck
            if(rememberChecked == 1){
                rememberChecked = 0
                changeRadioStatus(rememberChecked)
            }else{
                rememberChecked = 1
                changeRadioStatus(rememberChecked)
            }
        }

    }
    private fun showDeleteButton(editText: EditText,imageView: ImageView) {
        editText.doAfterTextChanged { s ->
            if (s.isNullOrEmpty()) {
                imageView.visibility = View.GONE
            } else {
                imageView.visibility = View.VISIBLE
            }

        }
    }

    //자동 로그인 & 아이디 저장 라디오버튼 check or uncheck 해주는 함수
    private fun changeRadioStatus(rememberChecked:Int){
        when(rememberChecked){
            0->{
                autoLoginBtn.setImageResource(R.drawable.checkboxempty)
                saveIdBtn.setImageResource(R.drawable.checkboxempty)
            }
            1->{
                autoLoginBtn.setImageResource(R.drawable.checkboxchecked)
                saveIdBtn.setImageResource(R.drawable.checkboxempty)
            }
            2->{
                autoLoginBtn.setImageResource(R.drawable.checkboxempty)
                saveIdBtn.setImageResource(R.drawable.checkboxchecked)
            }
        }
    }

    override fun loginErrorOccurred(){
        errorOccuredEditTextChangeUI(userPw)
        errorMsg.visibility=View.VISIBLE
    }

    private fun errorOccuredEditTextChangeUI(editText: EditText){
        editText.setBackgroundResource(R.drawable.red_edittext)
        changedError(editText)
    }
    private fun changedError(editText: EditText){
        editText.doAfterTextChanged { s->
            if(s.isNullOrEmpty()){

            }
            else{
                errorMsg.visibility = View.GONE
                editText.setBackgroundResource(R.drawable.gray_edittext_selector)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.e("msg","get google id :"+account?.account?.name)
                Log.e("msg","get google id :"+account?.id)
                Log.e("msg","get google id :"+account?.idToken)
                presenter.tryGoogleLogin(account?.idToken!!)
            } catch (e: ApiException) {
//                view.makeToast( "Google sign in failed:(")
                Log.e("msg", "error : ${e.message}")
                Log.e("msg", "status : ${e.statusCode}")
            }

        }
    }
    fun initNaver(){
        mOAuthLoginModule = OAuthLogin.getInstance()
        mContext = this
        mOAuthLoginModule.init(
            mContext
            , getString(R.string.naver_client_id)
            , getString(R.string.naver_client_secret)
            , getString(R.string.naver_client_name)
        )
    }
    fun initNaverHandler(){
        mOAuthLoginHandler = object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                    val accessToken: String = mOAuthLoginModule.getAccessToken(baseContext)
                    val refreshToken: String = mOAuthLoginModule.getRefreshToken(baseContext)
                    Log.d("msg","token:"+accessToken)
                    presenter.tryNaverLogin(accessToken)
                } else {
                    val errorCode: String = mOAuthLoginModule.getLastErrorCode(mContext).code
                    val errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext)

                    Toast.makeText(
                        baseContext, "errorCode:" + errorCode
                                + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
    }


}


