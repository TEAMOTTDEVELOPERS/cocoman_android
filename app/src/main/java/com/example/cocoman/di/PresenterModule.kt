package com.example.cocoman.di

import com.example.cocoman.initrate.InitialRatingContract
import com.example.cocoman.initrate.InitialRatingPresenter
import com.example.cocoman.login.LoginContract
import com.example.cocoman.login.LoginPresenter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class PresenterModule {

    @Binds
    abstract fun bindLoginPresenter(impl: LoginPresenter): LoginContract.Presenter

    @Binds
    abstract fun bindInitialRatingPresenter(impl: InitialRatingPresenter): InitialRatingContract.Presenter
}