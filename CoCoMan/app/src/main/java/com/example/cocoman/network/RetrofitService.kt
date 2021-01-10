package com.example.cocoman.network

import com.example.cocoman.data.*
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.*


interface RetrofitService{
    @POST("/login/")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginToken>

}