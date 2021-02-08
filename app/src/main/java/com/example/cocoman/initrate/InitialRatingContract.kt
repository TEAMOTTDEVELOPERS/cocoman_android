package com.example.cocoman.initrate

import com.example.cocoman.BaseContract
import com.example.cocoman.data.ContentRating

class InitialRatingContract {
    interface View : BaseContract.View {
        fun updateContents(contentList: List<ContentRating>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onDoneClick()
        fun onContentsRated(contentsId: String, p1: Float)
        fun onContentsUnrated(contentsId: String, p1: Float)
    }
}