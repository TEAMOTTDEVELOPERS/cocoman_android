package com.example.cocoman.data


data class Contents(
    var id:String,
    var actorList: List<Actor>,
    var broadcastDate: String,
    var country: String,
    var directorList: List<Director>,
    var genreList: List<Genre>,
    var gradeRate: String,
    var keywordList: List<Keyword>,
    var title: String,
    var year: Int,
    var story: String,
    var runningTime: Int,
    var posterPath: String,
    var openDate: String
)