package com.example.cocoman.data.source.request

data class LoginRequest(
    var provider: String,
    var userId: String?,
    var password: String?,
    var accessToken: String?
)
