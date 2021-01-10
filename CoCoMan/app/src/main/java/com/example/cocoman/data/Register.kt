package com.example.cocoman.data

data class Register(
    val username : String,
    val password1 : String,
    val password2 : String,
    val gender : String,
    val age: Int,
    val usedOTT: ArrayList<String>
)