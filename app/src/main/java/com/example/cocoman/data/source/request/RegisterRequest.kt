package com.example.cocoman.data.source.request

data class RegisterRequest(
    var registerType: String,
    var email: String?,
    var password: String?,
    var nickname: String,
    var gender: String,
    var authToken: String?
)