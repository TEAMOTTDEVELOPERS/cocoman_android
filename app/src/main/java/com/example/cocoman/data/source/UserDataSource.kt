package com.example.cocoman.data.source

import com.example.cocoman.data.MyInfo
import io.reactivex.rxjava3.core.Single

interface UserDataSource {
    fun getUser(): Single<MyInfo>
    fun saveUser(userInfo: MyInfo): Single<MyInfo>
}