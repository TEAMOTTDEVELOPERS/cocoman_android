package com.example.cocoman.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.cocoman.BaseContract


class LoginContract {
    interface View : BaseContract.View {
        fun loginErrorOccurred()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun tryLogin(email: String, password: String)
        fun tryKakaoLogin(context: Context)
        fun tryNaverLogin(activity: Activity)
        fun tryFacebookLogin(activity: Activity)
        fun tryGoogleLogin(context: Context)
        fun onGoogleLoginResult(data: Intent?)
        fun processFacebookResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}