package com.example.cocoman.rate

interface onContentRatingStatusChangeListener {
    fun onContentRated(position: Int, contentsId: String, score: Float)
    fun onContentUnrated(position: Int, contentsId: String, score: Float)
}