package com.example.cocoman.register

import com.example.cocoman.BaseContract

class RegisterContract {
    interface View: BaseContract.View {
        fun errorInInfo(errorField:String)

    }
    interface Presenter:BaseContract.Presenter<View>{
        fun trySignUp(username:String, userPassword:String, userPasswordCheck:String, userAge:String, userGender:String,userNickname:String)

    }
}