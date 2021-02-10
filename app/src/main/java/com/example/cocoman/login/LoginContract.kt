package com.example.cocoman.login

import android.content.Context
import com.example.cocoman.BaseContract
import com.facebook.login.widget.LoginButton
import com.nhn.android.naverlogin.OAuthLogin
import io.reactivex.rxjava3.disposables.CompositeDisposable


class LoginContract {
    interface View : BaseContract.View {
        fun loginErrorOccurred()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun tryLogin(email: String, password: String)
        fun tryKakaoLogin(context: Context)
        fun tryGoogleLoginInit(context: Context)
        fun tryNaverLogin(token:String)
        fun tryFacebookLogin(context: Context,btn:LoginButton)
        fun tryGoogleLogin(token:String)
    }
}