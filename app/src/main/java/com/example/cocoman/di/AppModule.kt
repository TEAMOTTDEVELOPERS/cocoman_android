package com.example.cocoman.di

import com.example.cocoman.data.source.UserApi
import com.example.cocoman.login.LoginContract
import com.example.cocoman.login.LoginPresenter
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        //todo :: 레포지토리로 세팅해야함.
//        val header = Interceptor {
//            val original = it.request()
//            // 로그인 했으면 헤더 작성
//            if (checkIsLogin()) {
//                // 토큰 가져 오는데, 토큰이 null이 아닌 경우 이 부분 수행
//                getUserToken()?.let { token ->
//                    val request = original.newBuilder()
//                        .header("Authorization", "token " + token)
//                        .build()
//                    it.proceed(request)
//                }
//            }
//            // 로그인 하지 않았으면 헤더 작성할 필요 없음
//            else {
//                it.proceed(original)
//            }
//
//        }
        return OkHttpClient.Builder()
//            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl("http://3.138.79.170:8080/api/v1/")
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideGoogleSignInOption(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode("753386993138-ipr91dqokq943jjmoa9jsdpaf0h1qrmu.apps.googleusercontent.com")
            .requestIdToken("753386993138-ipr91dqokq943jjmoa9jsdpaf0h1qrmu.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

}