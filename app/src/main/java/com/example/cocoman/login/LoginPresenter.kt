package com.example.cocoman.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cocoman.R
import com.example.cocoman.activity.RegisterActivity
import com.example.cocoman.activity.RegisterForSocialLoginActivity
import com.example.cocoman.data.MyInfo
import com.example.cocoman.data.source.UserApi
import com.example.cocoman.data.source.request.LoginRequest
import com.example.cocoman.initrate.InitialRatingActivity
import com.facebook.*
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.stetho.server.http.HttpStatus
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class LoginPresenter @Inject constructor(
    private val userApi: UserApi,
    private val googleSignInOptions: GoogleSignInOptions
) : LoginContract.Presenter {
    private lateinit var view: LoginContract.View
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mOAuthLoginModule : OAuthLogin
    lateinit var  mOAuthLoginHandler: OAuthLoginHandler

    private fun checkServerResponse(response:String){
        if(response.substring(46,49)=="500"){
            view.makeToast("500 internal server error")
            Log.e("coconut", "500:"+response.substring(46,49))
        }
        else if(response.substring(46,49)=="400"){
            view.makeToast("아이디와 비번을 다시 확인해주세요!")
            Log.e("coconut", "400:"+response.substring(46,49))
        }
        else{
            view.makeToast(response)
        }

    }

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
                    val errorMsg=err.toString()
                    checkServerResponse(errorMsg)
                    view.loginErrorOccurred()
                }
            })
            .apply { compositeDisposable.add(this) }

    }

    override fun tryKakaoLogin(context: Context) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        view.makeToast("접근이 거부 됨(동의 취소)")
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        view.makeToast("유효하지 않은 앱")
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        view.makeToast("인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        view.makeToast("요청 파라미터 오류")
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        view.makeToast("유효하지 않은 scope ID")
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        view.makeToast("설정이 올바르지 않음(android key hash)")
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        view.makeToast("서버 내부 에러")
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        view.makeToast("앱이 요청 권한이 없음")
                    }
                    else -> { // Unknown
                        view.makeToast("기타 에러")
                    }
                }
            }
            else if (token != null) {
                view.makeToast( "로그인에 성공하였습니다.")
                userApi.login(LoginRequest("kakao", null, null, token.toString()))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { view.startLoading() }
                    .subscribe({ myInfo ->
                        //TODO: 해당 유저가 initialrating 했는지 안했는지 여부! ==> 했으면 main으로!
                        view.makeToast("login success with ${myInfo.id}")
                        view.navigateWithFinish(InitialRatingActivity::class)
                    }, { err ->
                        run {
                            val errorMsg=err.toString()
                            checkServerResponse(errorMsg)
                        }
                    })
                    .apply { compositeDisposable.add(this) }
            }
        }

        if(LoginClient.instance.isKakaoTalkLoginAvailable(context)){
            LoginClient.instance.loginWithKakaoTalk(context, callback = callback)
        }else{
            LoginClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    override fun tryNaverLogin(token: String) {
        userApi.login(LoginRequest("naver", null, null, token))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.startLoading() }
            .subscribe({ myInfo ->
                view.makeToast("login success with ${myInfo.id}")
                view.navigateWithFinish(InitialRatingActivity::class)
            }, { err ->
                run {
                    val errorMsg=err.toString()
                    checkServerResponse(errorMsg)
                }
            })
            .apply { compositeDisposable.add(this) }
    }

    override fun tryFacebookLogin(context: Context,btn: LoginButton) {
        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(context)
        val callbackManager = CallbackManager.Factory.create()
//        facebookBtn.setReadPermissions("email","public_profile")
        btn.registerCallback(callbackManager,object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                if(result?.accessToken!=null){
                    val accessToken = result.accessToken
                    userApi.login(LoginRequest("facebook", null, null, accessToken.toString()))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { view.startLoading() }
                        .subscribe({ myInfo ->
                            //TODO: 해당 유저가 initialrating 했는지 안했는지 여부! ==> 했으면 main으로!
                            view.makeToast("login success with ${myInfo.id}")
                            view.navigateWithFinish(InitialRatingActivity::class)
                        }, { err ->
                            run {
                                val errorMsg=err.toString()
                                Log.e("coconut", err.toString())
                                if(errorMsg.substring(46,49)=="500"){
                                    view.makeToast("500 internal server error")
                                    Log.e("coconut", "500:"+errorMsg.substring(46,49))
                                }
                                else if(errorMsg.substring(46,49)=="400"){
                                    // 400 이면 해당 아이디 & 비번 가진 유저 없다는 뜻 --> 소셜 회원가입으로!
                                    view.navigateWithFinish(RegisterActivity::class)
                                }
                                else{view.makeToast(err.message)
                                    Log.e("coconut", errorMsg)
                                }
                            }
                        })
                        .apply { compositeDisposable.add(this) }
//                    getFacebookInfo(accessToken)

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
    override fun tryGoogleLoginInit(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode("753386993138-ipr91dqokq943jjmoa9jsdpaf0h1qrmu.apps.googleusercontent.com")
            .requestIdToken("753386993138-ipr91dqokq943jjmoa9jsdpaf0h1qrmu.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        view.navigateActivityForResult(signInIntent,RC_SIGN_IN)
    }

    override fun tryGoogleLogin(token: String) {
        userApi.login(LoginRequest("google", null, null, token))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.startLoading() }
            .subscribe({ myInfo ->
                view.makeToast("login success with ${myInfo.id}")
                view.navigateWithFinish(InitialRatingActivity::class)
            }, { err ->
                run {
                    val errorMsg=err.toString()
                    checkServerResponse(errorMsg)
                }
            })
            .apply { compositeDisposable.add(this) }
    }


    override fun attach(view: LoginContract.View) {
        this.view = view
    }

    override fun detach() {
        compositeDisposable.clear()
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        callbackManager.onActivityResult(requestCode,resultCode,data)
//        view.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                Log.e("msg","get google id :"+account?.account?.name)
//                Log.e("msg","get google id :"+account?.id)
//                Log.e("msg","get google id :"+account?.idToken)
//                userApi.login(LoginRequest("gmail", null, null, account?.idToken.toString()))
//                    .subscribeOn(Schedulers.computation())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnSubscribe { view.startLoading() }
//                    .subscribe({ myInfo ->
//                        //TODO: 해당 유저가 initialrating 했는지 안했는지 여부! ==> 했으면 main으로!
//                        view.makeToast("login success with ${myInfo.id}")
//                        view.navigateWithFinish(InitialRatingActivity::class)
//                    }, { err ->
//                        run {
//                            val errorMsg=err.toString()
//                            Log.e("coconut", err.toString())
//                            if(errorMsg.substring(46,49)=="500"){
//                                view.makeToast("500 internal server error")
//                                Log.e("coconut", "500:"+errorMsg.substring(46,49))
//                            }
//                            else if(errorMsg.substring(46,49)=="400"){
//                                // 400 이면 해당 아이디 & 비번 가진 유저 없다는 뜻 --> 소셜 회원가입으로!
//                                view.navigateWithFinish(RegisterActivity::class)
//                            }
//                            else{view.makeToast(err.message)
//                                Log.e("coconut", errorMsg)
//                            }
//                        }
//                    })
//                    .apply { compositeDisposable.add(this) }
//            } catch (e: ApiException) {
//                view.makeToast( "Google sign in failed:(")
//                Log.e("msg", "error : ${e.message}")
//                Log.e("msg", "status : ${e.statusCode}")
//            }
//        }
//    }
}