package com.example.cocoman.login

import android.os.Bundle
import android.widget.*
import com.example.cocoman.BaseActivity
import com.example.cocoman.R
import com.example.cocoman.activity.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity(), LoginContract.View {
    @Inject
    lateinit var presenter: LoginContract.Presenter
    private lateinit var emailView: TextView
    private lateinit var passwordView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()

        findViewById<Button>(R.id.login_btn).apply {
            setOnClickListener {
                presenter.tryLogin(
                    emailView.text.toString(),
                    passwordView.text.toString()
                )
            }
        }

        findViewById<Button>(R.id.register_btn).apply {
            setOnClickListener { navigateWithFinish(RegisterActivity::class) }
        }

        findViewById<ImageButton>(R.id.login_kakao).apply {
            setOnClickListener { presenter.tryKakaoLogin(this@LoginActivity) }
        }

    }

    private fun initView() {
        presenter.attach(this)
        emailView = findViewById(R.id.insert_id)
        passwordView = findViewById(R.id.insert_pw)

    }

}


