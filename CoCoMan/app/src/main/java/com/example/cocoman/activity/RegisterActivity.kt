package com.example.cocoman.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.cocoman.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var usernameView: EditText
    lateinit var userPasswordView: EditText
    lateinit var userPasswordCheckView:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView(this@RegisterActivity)

        make_account.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun initView(activity: Activity){
        usernameView = activity.findViewById(R.id.id_register)
        userPasswordView = activity.findViewById(R.id.password_register)
        userPasswordCheckView = activity.findViewById(R.id.passwordCheck_register)
    }

    fun getUsername():String{
        return usernameView.text.toString()
    }
    fun getUserPassword():String{
        return userPasswordView.text.toString()
    }
    fun getUserPasswordCheck():String{
        return userPasswordCheckView.text.toString()
    }
}