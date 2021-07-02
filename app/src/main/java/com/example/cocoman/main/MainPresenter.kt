package com.example.cocoman.main

import com.example.cocoman.data.source.UserApi
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainPresenter  @Inject constructor(
    private val userApi: UserApi
): MainContract.Presenter{
    private lateinit var view: MainContract.View
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun changeFragment(index: Int, doneRate: Int) {
        TODO("Not yet implemented")
    }

    override fun attach(view: MainContract.View) {
        this.view = view
    }

    override fun detach() {
        compositeDisposable.clear()
    }

}