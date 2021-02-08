package com.example.cocoman.initrate

import com.example.cocoman.data.ContentRating

interface onContentRatingStatusChangeListener {
    fun onContentRated(position: Int, contentsId: String, score: Float)
    fun onContentUnrated(position: Int, contentsId: String, score: Float)
}