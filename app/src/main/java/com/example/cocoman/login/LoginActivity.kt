package com.example.cocoman.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.cocoman.BaseActivity
import com.example.cocoman.R
import com.example.cocoman.activity.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity(), LoginContract.View {
    @Inject
    lateinit var presenter: LoginContract.Presenter
    private lateinit var userId: EditText
    private lateinit var userPw: EditText
    lateinit var loginBtn: Button
    lateinit var registerBtn: TextView
    lateinit var findPwBtn: TextView
    lateinit var findIDBtn: TextView
    lateinit var deleteIDBtn: ImageView
    lateinit var deletePwBtn: ImageView
    lateinit var personIcon: ImageView
    lateinit var lockIcon: ImageView
    lateinit var errorMsg: TextView
    lateinit var fbBtn: ImageView
    lateinit var googleBtn: ImageView
    lateinit var kakaoBtn: ImageView
    lateinit var naverBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
        fbBtn = findViewById(R.id.login_facebook)
        googleBtn = findViewById(R.id.login_google)
        kakaoBtn = findViewById(R.id.login_kakao)
        naverBtn = findViewById(R.id.login_naver)
        personIcon = findViewById(R.id.insert_id_personicon)
        lockIcon = findViewById(R.id.insert_pw_lockicon)
        errorMsg = findViewById(R.id.error_msg_login)
    }

    private fun setupListener() {
        loginBtn.setOnClickListener {
            presenter.tryLogin(
                userId.text.toString(),
                userPw.text.toString()
            )
        }
        registerBtn.setOnClickListener { navigateWithFinish(RegisterActivity::class) }
        kakaoBtn.setOnClickListener { presenter.tryKakaoLogin(this) }
        googleBtn.setOnClickListener { presenter.tryGoogleLogin(this) }
        fbBtn.setOnClickListener { presenter.tryFacebookLogin(this) }
        naverBtn.setOnClickListener { presenter.tryNaverLogin(this) }
        findIDBtn.setOnClickListener {}
        findPwBtn.setOnClickListener {}

        // 사람 이미지 바꾸기
        userId.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                personIcon.setImageResource(R.drawable.insertidaf)
            else
                personIcon.setImageResource(R.drawable.insertidbf)
        }

        userPw.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                lockIcon.setImageResource(R.drawable.insertpwaf)
            else
                lockIcon.setImageResource(R.drawable.insertpwbf)
        }

        //edittext 필드에 text 있으면 x 버튼 표시, 없으면 x 버튼도 없애기
        showDeleteButton(userPw, deletePwBtn)
        showDeleteButton(userId, deleteIDBtn)
        deletePwBtn.setOnClickListener { userPw.setText("") }
        deleteIDBtn.setOnClickListener { userId.setText("") }


    }

    private fun showDeleteButton(editText: EditText, imageView: ImageView) {
        editText.doAfterTextChanged { s ->
            if (s.isNullOrEmpty()) {
                imageView.visibility = View.GONE
            } else {
                imageView.visibility = View.VISIBLE
            }

        }
    }

    override fun loginErrorOccurred() {
        errorOccuredEditTextChangeUI(userPw)
        errorMsg.visibility = View.VISIBLE
    }

    private fun errorOccuredEditTextChangeUI(editText: EditText) {
        editText.setBackgroundResource(R.drawable.red_edittext)
        changedError(editText)
    }

    private fun changedError(editText: EditText) {
        editText.doAfterTextChanged { s ->
            if (s.isNullOrEmpty()) {

            } else {
                errorMsg.visibility = View.GONE
                editText.setBackgroundResource(R.drawable.gray_edittext_selector)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.processFacebookResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            presenter.onGoogleLoginResult(data)
        }
    }


}


