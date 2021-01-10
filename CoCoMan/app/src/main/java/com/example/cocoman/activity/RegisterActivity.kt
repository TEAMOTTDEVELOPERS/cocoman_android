package com.example.cocoman.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import com.example.cocoman.R
import com.example.cocoman.data.Register
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var usernameView: EditText
    lateinit var userPasswordView: EditText
    lateinit var userPasswordCheckView:EditText
    lateinit var userGender: RadioButton
    lateinit var userAge:EditText
    lateinit var createAccountBtn : Button
    var ottContents=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView(this@RegisterActivity)
        setupListener()

    }
    fun setupListener(){
        createAccountBtn.setOnClickListener {
            register()
        }
    }

    fun initView(activity: Activity){
        usernameView = activity.findViewById(R.id.id_register)
        userPasswordView = activity.findViewById(R.id.password_register)
        userPasswordCheckView = activity.findViewById(R.id.passwordCheck_register)
        userAge = activity.findViewById(R.id.age_register)
        createAccountBtn = activity.findViewById(R.id.make_account)
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
    fun getUserAge():Int{
        return userAge.text.toString().toInt()
    }
    fun getUserGender():String{
        if(gender_female.isChecked){
            return "Female"
        }
        else if(gender_male.isChecked){
            return "Male"
        }
        else{
            return "Etc"
        }
    }

    fun register(){
        //egister(getUsername(),getUserPassword(),getUserPasswordCheck(),getUserGender(),getUserAge(),ottContents)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onCheckboxClicked(view: View) {
        if(view is CheckBox){
            val checked : Boolean = view.isChecked
            when(view.id){
                R.id.netflix -> {
                    if(checked){
                        ottContents.add("Netflix")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Netflix")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.watcha -> {
                    if(checked){
                        ottContents.add("Watcha")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Watcha")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.tving -> {
                    if(checked){
                        ottContents.add("Tving")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Tving")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.naver -> {
                    if(checked){
                        ottContents.add("Naver")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Naver")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.kakaoTv -> {
                    if(checked){
                        ottContents.add("Kakao Tv")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Kakao Tv")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.wavve -> {
                    if(checked){
                        ottContents.add("Wavve")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Wavve")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.seezen -> {
                    if(checked){
                        ottContents.add("Seezen")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Seezen")
                        Log.d("contents",""+ottContents)
                    }
                }
                R.id.coupangPlay -> {
                    if(checked){
                        ottContents.add("Coupang Play")
                        Log.d("contents",""+ottContents)
                    }
                    else{
                        ottContents.remove("Coupang Play")
                        Log.d("contents",""+ottContents)
                    }
                }
            }
        }
    }
}