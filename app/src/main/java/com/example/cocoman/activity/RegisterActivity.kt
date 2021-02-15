//package com.example.cocoman.activity
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.View
//import android.widget.*
//import com.example.cocoman.R
//import com.example.cocoman.login.LoginActivity
//
//class RegisterActivity : AppCompatActivity() {
//
//    private lateinit var genderFemale : ImageView
//    private lateinit var genderMale: ImageView
//    private lateinit var genderEtc: ImageView
//    lateinit var errorMentView: TextView
//    lateinit var errorMentContractView:TextView
//    lateinit var usernameView: EditText
//    lateinit var userPasswordView: EditText
//    lateinit var userPasswordCheckView:EditText
//    lateinit var userAge:EditText
//    lateinit var deleteUsernameBtn: ImageView
//    lateinit var deletePasswordBtn: ImageView
//    lateinit var deletePasswordCheckBtn: ImageView
//    lateinit var nextBtn : Button
//    lateinit var goBackBtn: ImageView
//    // genderChecked --> 0 = none, 1 = male, 2 = female, 3 = etc
//    var genderChecked:Int = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//        initView()
//        setupListener()
//
//    }
//    fun setupListener(){
//        nextBtn.setOnClickListener {
//            val canProceed = checkInfo()
//            if(canProceed){
//                val intent = Intent(this@RegisterActivity, SelectOTTActivity::class.java)
//                intent.putExtra("username",getUsername().toString())
//                intent.putExtra("userAge",getUserAge().toString())
//                intent.putExtra("userPassword",getUserPassword().toString())
//                intent.putExtra("userGender",getUserGender().toString())
//                startActivity(intent)
//                finish()
//            }
//        }
//        showDeleteButton(usernameView,deleteUsernameBtn)
//        showDeleteButton(userPasswordView,deletePasswordBtn)
//        showDeleteButton(userPasswordCheckView,deletePasswordCheckBtn)
//        deleteUsernameBtn.setOnClickListener {
//            usernameView.setText("")
//        }
//        deletePasswordBtn.setOnClickListener {
//            userPasswordView.setText("")
//        }
//        deletePasswordCheckBtn.setOnClickListener {
//            userPasswordCheckView.setText("")
//        }
//        genderMale.setOnClickListener {
//            // 원래 체크 되어있었는데 한번더 누름 --> uncheck
//            if(genderChecked == 1){
//                genderChecked = 0
//                changeRadioStatus(genderChecked)
//            }else{
//                genderChecked = 1
//                changeRadioStatus(genderChecked)
//                errorMentView.visibility = View.GONE
//            }
//        }
//        genderFemale.setOnClickListener {
//            // 원래 체크 되어있었는데 한번더 누름 --> uncheck
//            if(genderChecked == 2){
//                genderChecked = 0
//                changeRadioStatus(genderChecked)
//            }else{
//                genderChecked = 2
//                changeRadioStatus(genderChecked)
//                errorMentView.visibility = View.GONE
//            }
//        }
//        genderEtc.setOnClickListener {
//            // 원래 체크 되어있었는데 한번더 누름 --> uncheck
//            if(genderChecked == 3){
//                genderChecked = 0
//                changeRadioStatus(genderChecked)
//            }else{
//                genderChecked = 3
//                changeRadioStatus(genderChecked)
//                errorMentView.visibility = View.GONE
//            }
//        }
//        goBackBtn.setOnClickListener {
//            val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    fun initView(){
//        usernameView = findViewById(R.id.insert_id_register)
//        userPasswordView = findViewById(R.id.insert_pw_register)
//        userPasswordCheckView = findViewById(R.id.insert_rePw_register)
//        userAge = findViewById(R.id.insert_age_register)
//        nextBtn = findViewById(R.id.nextBtn_register)
//        genderFemale = findViewById(R.id.genderBtn_female_register)
//        genderMale = findViewById(R.id.genderBtn_male_register)
//        genderEtc  = findViewById(R.id.genderBtn_etc_register)
//        errorMentView = findViewById(R.id.error_msg_inputfield_register)
//        errorMentContractView = findViewById(R.id.error_msg_contract_register)
//        deleteUsernameBtn = findViewById(R.id.delete_id_register)
//        deletePasswordBtn = findViewById(R.id.delete_pw_register)
//        deletePasswordCheckBtn = findViewById(R.id.delete_rePw_register)
//        goBackBtn=findViewById(R.id.gobackBtn_register)
//    }
//
//    fun getUsername():String{
//        return usernameView.text.toString()
//    }
//    fun getUserPassword():String{
//        return userPasswordView.text.toString()
//    }
//    fun getUserPasswordCheck():String{
//        return userPasswordCheckView.text.toString()
//    }
//    fun getUserAge():String{
//        return userAge.text.toString()
//    }
//    fun getUserGender():String{
//        var genderString=""
//        when(genderChecked){
//            0 -> {
//                genderString= "none"
//            }
//            1->{
//                genderString= "male"
//            }
//            2 ->{
//                genderString= "female"
//            }
//            3->{
//                genderString= "etc"
//            }
//        }
//        return genderString
//    }
//
//    fun checkInfo():Boolean{
//        if(checkDup()){
//            errorMentView.visibility = View.VISIBLE
//            errorMentView.setText("이미 계정이 있는 이메일 주소입니다")
//            errorOccuredEditTextChangeUI(usernameView)
//            return false
//        }
//        if(getUserPassword().isEmpty() && getUserPasswordCheck().isEmpty()){
//            errorMentView.visibility = View.VISIBLE
//            errorMentView.setText("비밀번호를 입력해주세요")
//            errorOccuredEditTextChangeUI(userPasswordCheckView,userPasswordView)
//            return false
//        }
//        if(getUserPassword() != getUserPasswordCheck()){
//            errorMentView.visibility = View.VISIBLE
//            errorMentView.setText("비밀번호가 일치하지 않습니다")
//            errorOccuredEditTextChangeUI(userPasswordCheckView,userPasswordView)
//            return false
//        }
//        if(getUserAge().isEmpty()){
//            errorMentView.visibility = View.VISIBLE
//            errorMentView.setText("나이를 입력해주세요")
//            errorOccuredEditTextChangeUI(userAge)
//            return false
//        }
//        if(getUserGender()=="none"){
//            errorMentView.visibility = View.VISIBLE
//            errorMentView.setText("성별을 선택해주세요")
//            return false
//        }
//        else {
//            return true
//        }
//    }
//
//    fun checkDup():Boolean{
//        return false
//        // TODO:아이디 중복체크 (if true --> have duplicate, else can proceed)
//    }
//    fun errorOccuredEditTextChangeUI(editText: EditText){
//        editText.setBackgroundResource(R.drawable.red_edittext)
//        changedError(editText)
//    }
//    fun errorOccuredEditTextChangeUI(editText1: EditText,editText2: EditText){
//        editText1.setBackgroundResource(R.drawable.red_edittext)
//        editText2.setBackgroundResource(R.drawable.red_edittext)
//        changedError(editText1,editText2)
//        changedError(editText2,editText1)
//    }
//
////    private fun showDeleteButton(editText: EditText,imageView: ImageView){
////        editText.addTextChangedListener(object : TextWatcher {
////            override fun afterTextChanged(s: Editable?) {
////                if (s != null) {
////                    if(s.isNotEmpty()){
////                        imageView.visibility = View.VISIBLE
////                    }else{
////                        imageView.visibility = View.GONE
////                    }
////                }
////            }
////
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
////                if (s != null) {
////                    if(s.isNotEmpty()){
////                        imageView.visibility = View.VISIBLE
////                    }else{
////                        imageView.visibility = View.GONE
////                    }
////                }
////            }
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////                if (s != null) {
////                    if(s.isNotEmpty()){
////                        imageView.visibility = View.VISIBLE
////                    }else{
////                        imageView.visibility = View.GONE
////                    }
////                }
////            }
////        })
////    }
//
////    private fun changedError(editText: EditText){
////        editText.addTextChangedListener(object : TextWatcher {
////            override fun afterTextChanged(s: Editable?) {
////                if (s != null) {
////                    if(s.isNotEmpty()){
////                        errorMentView.visibility = View.GONE
////                        editText.setBackgroundResource(R.drawable.gray_edittext_selector)
////                    }else{
////
////                    }
////                }
////            }
////
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
////                if (s != null) {
////                    if(s.isNotEmpty()){
////
////                    }else{
////
////                    }
////                }
////            }
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////                errorMentView.visibility = View.GONE
////                if (s != null) {
////                    if(s.isNotEmpty()){
////                        errorMentView.visibility = View.GONE
////                        editText.setBackgroundResource(R.drawable.gray_edittext_selector)
////                    }else{
////                    }
////                }
////            }
////        })
////    }
//
//    //CHECK!!
////    private fun changedError(editText1: EditText,editText2: EditText){
////        editText1.addTextChangedListener(object : TextWatcher {
////            override fun afterTextChanged(s: Editable?) {
////                if (s != null) {
////                    if(s.isNotEmpty()){
////                        errorMentView.visibility = View.GONE
////                        editText1.setBackgroundResource(R.drawable.gray_edittext_selector)
////                        editText2.setBackgroundResource(R.drawable.gray_edittext_selector)
////                    }else{
////
////                    }
////                }
////            }
////
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
////                if (s != null) {
////                    if(s.isNotEmpty()){
////
////                    }else{
////
////                    }
////                }
////            }
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////                errorMentView.visibility = View.GONE
////                if (s != null) {
////                    if(s.isNotEmpty()){
////                        errorMentView.visibility = View.GONE
////                        editText1.setBackgroundResource(R.drawable.gray_edittext_selector)
////                        editText2.setBackgroundResource(R.drawable.gray_edittext_selector)
////                    }else{
////                    }
////                }
////            }
////        })
////    }
////    private fun changeRadioStatus(genderChecked:Int){
////        when(genderChecked){
////            0->{
////                genderFemale.setImageResource(R.drawable.checkboxempty)
////                genderMale.setImageResource(R.drawable.checkboxempty)
////                genderEtc.setImageResource(R.drawable.checkboxempty)
////            }
////            1->{
////                genderFemale.setImageResource(R.drawable.checkboxempty)
////                genderMale.setImageResource(R.drawable.checkboxchecked)
////                genderEtc.setImageResource(R.drawable.checkboxempty)
////            }
////            2->{
////                genderMale.setImageResource(R.drawable.checkboxempty)
////                genderFemale.setImageResource(R.drawable.checkboxchecked)
////                genderEtc.setImageResource(R.drawable.checkboxempty)
////            }
////            3->{
////                genderMale.setImageResource(R.drawable.checkboxempty)
////                genderFemale.setImageResource(R.drawable.checkboxempty)
////                genderEtc.setImageResource(R.drawable.checkboxchecked)
////            }
////        }
////    }
//
//}