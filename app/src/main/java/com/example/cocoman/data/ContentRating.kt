package com.example.cocoman.data

data class ContentRating(
    var id: String,
    var title: String,
    //var poster: String,
    var year: Int,
    var contentType: String,
    var contentGenre: String,
    var score: Float
)