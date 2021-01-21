package com.example.cocoman.`interface`

import com.example.cocoman.data.ContentRating

interface onContentRatingStatusChangeListener {
    fun onContentRated(position: Int,score:Float): Unit
    fun onContentUnrated(position: Int,score: Float):Unit
}