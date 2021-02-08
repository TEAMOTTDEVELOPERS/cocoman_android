package com.example.cocoman.data.source

import com.example.cocoman.data.MyInfo
import com.example.cocoman.data.source.request.LoginRequest
import com.example.cocoman.data.source.request.RegisterRequest
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("user/signUp")
    fun register(request: RegisterRequest): Single<MyInfo>

    @POST("user/signIn")
    fun login(@Body request: LoginRequest): Single<MyInfo>
}