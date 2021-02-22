package com.example.cocoman.register

import android.view.View
import com.example.cocoman.activity.MainActivity
import com.example.cocoman.data.source.UserApi
import com.example.cocoman.data.source.request.LoginRequest
import com.example.cocoman.data.source.request.RegisterRequest
import com.example.cocoman.initrate.InitialRatingActivity
import com.example.cocoman.login.LoginActivity
import com.example.cocoman.login.LoginContract
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class RegisterPresenter @Inject constructor(
    private val userApi: UserApi
): RegisterContract.Presenter{
    private lateinit var view: RegisterContract.View
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    override fun trySignUp(username:String, userPassword:String, userPasswordCheck: String, userAge:String, userGender:String,userNickname:String){
        val resultOfCheckInfo = checkInfo(username,userPassword,userPasswordCheck,userAge,userGender,userNickname)
        if(resultOfCheckInfo == "complete") {
            view.makeToast("complete 부분 실행 중")
            userApi.register(RegisterRequest("coconut", username, userPassword, userNickname, userGender,""))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.startLoading() }
                .subscribe({ myInfo ->
                    view.makeToast(("register success!!"))
                    view.navigateWithFinish(MainActivity::class)
                }, { err ->
                    run {
                        view.makeToast("error:" + err.toString())
                    }
                })
                .apply { compositeDisposable.add(this) }
        }else{
            view.errorInInfo(resultOfCheckInfo)
        }
    }


    override fun attach(view: RegisterContract.View) {
        this.view = view
    }

    override fun detach() {
        compositeDisposable.clear()
    }

    private fun checkDuplicateId(usernameView: String):Boolean{
        return false
        //TODO: ID 중복 확인!
    }

    private  fun checkInfo(username:String,userPassword: String,userPasswordCheck:String,userAge: String,userGender: String,userNickname: String):String{
        if(checkDuplicateId(username)){
            return "username"
        }
        if(userPassword.isEmpty() || userPasswordCheck.isEmpty()){
            return "password not inserted"
        }
        if(userPassword != userPasswordCheck){
            return "password not same"
        }
        if(userAge.isEmpty()){
            return "userAge"
        }
        if(userGender=="none"){
            return "userGender"
        }
        if(userNickname == ""){
            return "userNickname"
        }
        else {
            return "complete"
        }
    }
}