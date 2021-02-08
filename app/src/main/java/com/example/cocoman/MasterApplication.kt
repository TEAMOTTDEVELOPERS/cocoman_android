package com.example.cocoman

import android.app.Application
import android.content.Context
import com.example.cocoman.network.RetrofitService
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@HiltAndroidApp
class MasterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "e2aede34f771b87b113aabd6b0f9dd3c")
    }

}