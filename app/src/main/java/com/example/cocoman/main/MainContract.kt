package com.example.cocoman.main

import com.example.cocoman.BaseContract

class MainContract {
    interface View : BaseContract.View {

    }

    interface Presenter : BaseContract.Presenter<View> {
        fun changeFragment(index: Int, doneRate: Int)
    }
}