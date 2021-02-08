package com.example.cocoman.login

import android.content.Context
import android.util.Log
import com.example.cocoman.data.MyInfo
import com.example.cocoman.data.source.UserApi
import com.example.cocoman.data.source.request.LoginRequest
import com.example.cocoman.initrate.InitialRatingActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class LoginPresenter @Inject constructor(
    private val userApi: UserApi,
    private val googleSignInOptions: GoogleSignInOptions
) : LoginContract.Presenter {

    private lateinit var view: LoginContract.View
    private lateinit var googleSignInClient: GoogleSignInClient
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun tryLogin(email: String, password: String) {
        userApi.login(LoginRequest("coconut", email, password, null))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.startLoading() }
            .subscribe({ myInfo ->
                view.makeToast("login success with ${myInfo.id}")
                view.navigateWithFinish(InitialRatingActivity::class)
            }, { err ->
                run {
                    Log.e("coconut", err.toString())
                    view.makeToast(err.message)
                }
            })
            .apply { compositeDisposable.add(this) }

    }

    override fun tryKakaoLogin(context: Context) {

    }


    override fun attach(view: LoginContract.View) {
        this.view = view
    }

    override fun detach() {
        compositeDisposable.clear()
    }

}