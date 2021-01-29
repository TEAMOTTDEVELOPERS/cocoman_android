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

class RegisterActivity : AppCompatActivity() {

    private lateinit var genderFemale : RadioButton
    private lateinit var genderMale: RadioButton
    lateinit var usernameView: EditText
    lateinit var userPasswordView: EditText
    lateinit var userPasswordCheckView:EditText
    lateinit var userAge:EditText
    lateinit var createAccountBtn : Button
    lateinit var checkDuplicate : Button
    var ottContents=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
        setupListener()

    }
    fun setupListener(){
        createAccountBtn.setOnClickListener {
            register()
        }
        checkDuplicate.setOnClickListener {
            checkDup()
        }
    }

    fun initView(){
        usernameView = findViewById(R.id.id_register)
        userPasswordView = findViewById(R.id.password_register)
        userPasswordCheckView = findViewById(R.id.passwordCheck_register)
        userAge = findViewById(R.id.age_register)
        createAccountBtn = findViewById(R.id.make_account)
        checkDuplicate = findViewById(R.id.checkDuplicate_register)
        genderFemale = findViewById(R.id.gender_female)
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
        if(genderFemale.isChecked){
            return "Female"
        }
        else if(genderMale.isChecked){
            return "Male"
        }
        else{
            return "Etc"
        }
    }

    fun register(){
        // TODO: 서버랑 연결
        //register(getUsername(),getUserPassword(),getUserPasswordCheck(),getUserGender(),getUserAge(),ottContents)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun checkDup(){
        // TODO:아이디 중복체크
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