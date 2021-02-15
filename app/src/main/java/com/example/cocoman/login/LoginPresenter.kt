package com.example.cocoman.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.cocoman.R
import com.example.cocoman.data.source.UserApi
import com.example.cocoman.data.source.request.LoginRequest
import com.example.cocoman.initrate.InitialRatingActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.internal.ServerProtocol
import com.facebook.login.DefaultAudience
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.ref.WeakReference
import javax.inject.Inject

const val RC_SIGN_IN = 1

class LoginPresenter @Inject constructor(
    private val userApi: UserApi
) : LoginContract.Presenter {
    private val TAG = "LoginPresenter"
    private lateinit var view: LoginContract.View
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mOAuthLoginModule: OAuthLogin
    lateinit var mFacebookCallbackManager: CallbackManager

    private fun checkServerResponse(response: String) {
        if (response.substring(46, 49) == "500") {
            view.makeToast("500 internal server error")
            Log.e("coconut", "500:" + response.substring(46, 49))
        } else if (response.substring(46, 49) == "400") {
            view.makeToast("아이디와 비번을 다시 확인해주세요!")
            Log.e("coconut", "400:" + response.substring(46, 49))
        } else {
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
                    val errorMsg = err.toString()
                    checkServerResponse(errorMsg)
                    view.loginErrorOccurred()
                }
            })
            .apply { compositeDisposable.add(this) }
    }

    override fun tryKakaoLogin(context: Context) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "kakao login error : ${error.message}")
                view.makeToast("카카오 로그인 에러 : ${error.message}")
            } else {
                doSocialLogin("kakao", token?.accessToken)
            }
        }
        if (LoginClient.instance.isKakaoTalkLoginAvailable(context)) {
            LoginClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            LoginClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    override fun tryNaverLogin(activity: Activity) {
        mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(
            activity
            , activity.getString(R.string.naver_client_id)
            , activity.getString(R.string.naver_client_secret)
            , activity.getString(R.string.naver_client_name)
        )
        mOAuthLoginModule.startOauthLoginActivity(
            activity,
            NaverLoginHandler(activity) { success ->
                if (!success) {
                    val errorCode: String = mOAuthLoginModule.getLastErrorCode(activity).code
                    val errorDesc = mOAuthLoginModule.getLastErrorDesc(activity)
                    view.makeToast("네이버 로그인 오류 : $errorDesc, $errorCode")
                    return@NaverLoginHandler
                }
                val accessToken: String = mOAuthLoginModule.getAccessToken(activity)
                doSocialLogin("naver", accessToken)
            })

    }

    override fun tryFacebookLogin(activity: Activity) {
        val loginManager = LoginManager.getInstance()
        loginManager.defaultAudience = DefaultAudience.FRIENDS
        loginManager.loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK
        loginManager.authType = ServerProtocol.DIALOG_REREQUEST_AUTH_TYPE
        mFacebookCallbackManager = CallbackManager.Factory.create()

        loginManager.registerCallback(
            mFacebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d(TAG, "on facebook success~")
                    doSocialLogin("facebook", result?.accessToken?.token)
                }

                override fun onCancel() {
                    view.makeToast("페이스북 로그인 도중 취소하셨습니다.")
                }

                override fun onError(error: FacebookException?) {
                    view.makeToast("페이스북 로그인에 실패했습니다 : ${error?.message}")
                }
            })

        if (AccessToken.isCurrentAccessTokenActive()) {
            Log.d(TAG, "on facebook logout~")
            loginManager.logOut()
        }
        Log.d(TAG, "start facebook login~")
        loginManager.logInWithReadPermissions(
            activity,
            listOf("email", "public_profile")
        )
    }


    override fun tryGoogleLogin(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode("753386993138-ipr91dqokq943jjmoa9jsdpaf0h1qrmu.apps.googleusercontent.com")
            .requestIdToken("753386993138-ipr91dqokq943jjmoa9jsdpaf0h1qrmu.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        view.navigateActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onGoogleLoginResult(data: Intent?) {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            doSocialLogin("google", account?.idToken)

        } catch (e: ApiException) {
            view.makeToast("구글 로그인 실패 :  ${e.message}, ${e.statusCode}")
        }

    }

    override fun processFacebookResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun doSocialLogin(type: String, token: String?) {
        userApi.login(LoginRequest(type, null, null, token))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.startLoading() }
            .subscribe({ myInfo ->
                view.makeToast("login success with ${myInfo.id}")
                view.navigateWithFinish(InitialRatingActivity::class)
            }, { err ->
                //TODO 체크해서 가입페이지로 유도
                run {
                    val errorMsg = err.toString()
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


    private class NaverLoginHandler() : OAuthLoginHandler() {
        constructor(
            activity: Activity?,
            runnable: (success: Boolean) -> Unit
        ) : this() {
            this.mActivity = WeakReference<Activity>(activity)
            this.runnable = runnable
        }

        private lateinit var mActivity: WeakReference<Activity>
        private lateinit var runnable: (success: Boolean) -> Unit


        override fun run(success: Boolean) {
            runnable(success)
        }

    }

}