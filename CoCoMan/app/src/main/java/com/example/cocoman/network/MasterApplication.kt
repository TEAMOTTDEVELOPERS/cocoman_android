package com.example.cocoman.network

import android.app.Application
import android.content.Context
import com.example.cocoman.adapter.KakaoSDKAdapter
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.kakao.auth.KakaoSDK
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication : Application(){
    lateinit var retrofitService:RetrofitService
    //var token = ""
    // test위해서 일단 이전에 만든 서버로 연결해 둠!
    val baseUrl = "https://capstonebutton.kro.kr:9000"

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        createRetrofit()

        instance = this
        KakaoSDK.init(KakaoSDKAdapter())
    }
    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getGlobalApplicationContext(): MasterApplication {
        checkNotNull(instance) { "this application does not inherit com.kakao.GlobalApplication" }
        return instance!!
    }

    companion object {
        var instance: MasterApplication? = null
    }


    fun createRetrofit(){
        //통신 나갈떄 가로채서 개조! -- header 달아줌
        val header = Interceptor{
            val original = it.request()
            // 로그인 했으면 헤더 작성
            if(checkIsLogin()){
                // 토큰 가져 오는데, 토큰이 null이 아닌 경우 이 부분 수행
                getUserToken()?.let {token ->
                    val request = original.newBuilder()
                        .header("Authorization","token "+token)
                        .build()
                    it.proceed(request)
                }
            }
            // 로그인 하지 않았으면 헤더 작성할 필요 없음
            else{
                it.proceed(original)
            }

        }

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    // 로그인 했는지 안했는지는 sharedPreference의 토큰값으로 결정!
    fun checkIsLogin():Boolean{
        val sharedPreferences = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("login_sp","null")
        if(token != "null") return true
        else return false
    }

    fun getUserToken():String?{
        val sharedPreferences = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("login_sp","null")
        if(token=="null") return null
        else return token
    }
}