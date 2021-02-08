package com.example.cocoman.data.source.local

import com.example.cocoman.data.MyInfo
import com.example.cocoman.data.source.UserDataSource
import io.reactivex.rxjava3.core.Single


object LocalUserDataSource : UserDataSource {
    override fun getUser(): Single<MyInfo> {
        TODO("Not yet implemented")
    }

    override fun saveUser(userInfo: MyInfo): Single<MyInfo> {
        TODO("Not yet implemented")
    }
}