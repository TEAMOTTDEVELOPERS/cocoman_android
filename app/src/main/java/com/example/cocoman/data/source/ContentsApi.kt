package com.example.cocoman.data.source

import com.example.cocoman.data.Contents
import com.example.cocoman.data.MyInfo
import com.example.cocoman.data.source.request.LoginRequest
import com.example.cocoman.data.source.request.RegisterRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ContentsApi {
    @GET("contents")
    fun getRatingContents(): Observable<List<Contents>>

}