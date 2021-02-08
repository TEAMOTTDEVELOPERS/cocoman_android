package com.example.cocoman.login

import android.content.Context
import com.example.cocoman.BaseContract
import io.reactivex.rxjava3.disposables.CompositeDisposable


class LoginContract {
    interface View : BaseContract.View {

    }

    interface Presenter : BaseContract.Presenter<View> {
        fun tryLogin(email: String, password: String)
        fun tryKakaoLogin(context: Context)
    }
}