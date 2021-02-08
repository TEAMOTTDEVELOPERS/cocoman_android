package com.example.cocoman.data.source

import com.example.cocoman.data.MyInfo
import io.reactivex.rxjava3.core.Single

class UserRepository(
    private val userRemoteDataSource: UserDataSource,
    private val userLocalDataSource: UserDataSource
) : UserDataSource {
    private var cacheIsDirty = true
    override fun getUser(): Single<MyInfo> {
        if (!cacheIsDirty) {
            return userLocalDataSource.getUser()
        }

        return userRemoteDataSource.getUser()
            .doOnSuccess {
                cacheIsDirty = false
                userLocalDataSource.saveUser(it)
            }
    }

    override fun saveUser(userInfo: MyInfo): Single<MyInfo> {
        userRemoteDataSource.saveUser(userInfo)
        return userLocalDataSource.saveUser(userInfo)
    }

    fun markDirty() {
        cacheIsDirty = true
    }

}