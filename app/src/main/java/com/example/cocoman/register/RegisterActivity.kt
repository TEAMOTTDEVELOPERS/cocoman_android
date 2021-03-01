package com.example.cocoman.register

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import com.example.cocoman.BaseActivity
import com.example.cocoman.R
import com.example.cocoman.activity.MainActivity
import com.example.cocoman.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity :BaseActivity(),RegisterContract.View{
    @Inject
    lateinit var presenter: RegisterContract.Presenter
    private lateinit var genderFemale : ImageView
    private lateinit var genderMale: ImageView
    private lateinit var genderEtc: ImageView
    lateinit var errorMentView: TextView
    lateinit var errorMentContractView: TextView
    lateinit var usernameView: EditText
    lateinit var userPasswordView: EditText
    lateinit var userPasswordCheckView: EditText
    lateinit var userAgeBtn: Button
    lateinit var deleteUsernameBtn: ImageView
    lateinit var deletePasswordBtn: ImageView
    lateinit var deletePasswordCheckBtn: ImageView
    lateinit var nextBtn : Button
    lateinit var goBackBtn: ImageView
    lateinit var dialog:AlertDialog
    lateinit var userAge:NumberPicker
    lateinit var userAgeInserted:String
    lateinit var userNickName:EditText
    lateinit var deleteNickNameBtn: ImageView
    // genderChecked --> 0 = none, 1 = male, 2 = female, 3 = etc
    var genderChecked:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
        setupListener()
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.Q)
    fun setupListener(){
        nextBtn.setOnClickListener {
            presenter.trySignUp(usernameView.text.toString(),userPasswordView.text.toString(),userPasswordCheckView.text.toString(),userAgeInserted,getUserGender(),userNickName.toString())
        }
        showDeleteButton(usernameView,deleteUsernameBtn)
        showDeleteButton(userPasswordView,deletePasswordBtn)
        showDeleteButton(userPasswordCheckView,deletePasswordCheckBtn)
        showDeleteButton(userNickName,deleteNickNameBtn)
        deleteUsernameBtn.setOnClickListener {
            usernameView.setText("")
        }
        deletePasswordBtn.setOnClickListener {
            userPasswordView.setText("")
        }
        deletePasswordCheckBtn.setOnClickListener {
            userPasswordCheckView.setText("")
        }
        deleteNickNameBtn.setOnClickListener {
            userNickName.setText("")
        }
        genderMale.setOnClickListener {
            // 원래 체크 되어있었는데 한번더 누름 --> uncheck
            if(genderChecked == 1){
                genderChecked = 0
                changeRadioStatus(genderChecked)
            }else{
                genderChecked = 1
                changeRadioStatus(genderChecked)
                errorMentView.visibility = View.GONE
            }
        }
        genderFemale.setOnClickListener {
            // 원래 체크 되어있었는데 한번더 누름 --> uncheck
            if(genderChecked == 2){
                genderChecked = 0
                changeRadioStatus(genderChecked)
            }else{
                genderChecked = 2
                changeRadioStatus(genderChecked)
                errorMentView.visibility = View.GONE
            }
        }
        genderEtc.setOnClickListener {
            // 원래 체크 되어있었는데 한번더 누름 --> uncheck
            if(genderChecked == 3){
                genderChecked = 0
                changeRadioStatus(genderChecked)
            }else{
                genderChecked = 3
                changeRadioStatus(genderChecked)
                errorMentView.visibility = View.GONE
            }
        }
        goBackBtn.setOnClickListener {
            navigateWithFinish(LoginActivity::class)
        }

        userAgeBtn.setOnClickListener {
            dialog = AlertDialog.Builder(this@RegisterActivity).create()
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


            val edialog : LayoutInflater = LayoutInflater.from(this@RegisterActivity)
            val mView: View = edialog.inflate(R.layout.dialog_age,null)
            dialog.setView(mView)
            dialog.setCancelable(false)
            userAge = mView.findViewById(R.id.dialog_userage)
//            val dialogCancelBtn=mView.findViewById<Button>(R.id.dialog_cancel)
            val dialogOkBtn = mView.findViewById<Button>(R.id.dialog_ok)
            userAge.minValue=0
            userAge.maxValue=100
            setDividerColor(userAge, resources.getColor(R.color.white))


//            dialogCancelBtn.setOnClickListener {
//                dialog.dismiss()
//            }
            dialogOkBtn.setOnClickListener {
                userAgeInserted = userAge.value.toString()
                if(userAgeInserted==""){
                    makeToast("나이를 선택해주세요")
                }else{
                    userAgeBtn.setText(userAgeInserted)
                    dialog.dismiss()
                }

            }
            dialog.show()
        }


    }

    fun initView(){
        usernameView = findViewById(R.id.insert_id_register)
        userPasswordView = findViewById(R.id.insert_pw_register)
        userPasswordCheckView = findViewById(R.id.insert_rePw_register)
        userAgeBtn = findViewById(R.id.insert_age_register)
        nextBtn = findViewById(R.id.nextBtn_register)
        genderFemale = findViewById(R.id.genderBtn_female_register)
        genderMale = findViewById(R.id.genderBtn_male_register)
        genderEtc  = findViewById(R.id.genderBtn_etc_register)
        errorMentView = findViewById(R.id.error_msg_inputfield_register)
        errorMentContractView = findViewById(R.id.error_msg_contract_register)
        deleteUsernameBtn = findViewById(R.id.delete_id_register)
        deletePasswordBtn = findViewById(R.id.delete_pw_register)
        deletePasswordCheckBtn = findViewById(R.id.delete_rePw_register)
        goBackBtn=findViewById(R.id.gobackBtn_register)
        presenter.attach(this)
        userNickName=findViewById(R.id.insert_nickname_register)
        deleteNickNameBtn=findViewById(R.id.delete_nickname_register)
    }

    fun errorOccuredEditTextChangeUI(editText: EditText){
        editText.setBackgroundResource(R.drawable.red_edittext)
        changedError(editText)
    }
    fun errorOccuredEditTextChangeUI(editText1: EditText,editText2: EditText){
        editText1.setBackgroundResource(R.drawable.red_edittext)
        editText2.setBackgroundResource(R.drawable.red_edittext)
        changedError(editText1,editText2)
        changedError(editText2,editText1)
    }

    private fun showDeleteButton(editText: EditText,imageView: ImageView){
        editText.doAfterTextChanged { s->
            if(s.isNullOrEmpty()){
                imageView.visibility = View.GONE
            }else{
                imageView.visibility = View.VISIBLE
            }
        }
    }
    private fun changedError(editText: EditText){
        editText.doAfterTextChanged { s->
            if(s.isNullOrEmpty()){

            }else{
                errorMentView.visibility = View.GONE
                editText.setBackgroundResource(R.drawable.gray_edittext_selector)
            }
        }
    }

    private fun changedError(editText1: EditText,editText2: EditText){
        editText1.doAfterTextChanged { s->
            if(s.isNullOrEmpty()){

            }else{
                errorMentView.visibility = View.GONE
                editText1.setBackgroundResource(R.drawable.gray_edittext_selector)
                editText2.setBackgroundResource(R.drawable.gray_edittext_selector)
            }
        }
    }
    private fun changeRadioStatus(genderChecked:Int){
        when(genderChecked){
            0->{
                genderFemale.setImageResource(R.drawable.checkboxempty)
                genderMale.setImageResource(R.drawable.checkboxempty)
                genderEtc.setImageResource(R.drawable.checkboxempty)
            }
            1->{
                genderFemale.setImageResource(R.drawable.checkboxempty)
                genderMale.setImageResource(R.drawable.checkboxchecked)
                genderEtc.setImageResource(R.drawable.checkboxempty)
            }
            2->{
                genderMale.setImageResource(R.drawable.checkboxempty)
                genderFemale.setImageResource(R.drawable.checkboxchecked)
                genderEtc.setImageResource(R.drawable.checkboxempty)
            }
            3->{
                genderMale.setImageResource(R.drawable.checkboxempty)
                genderFemale.setImageResource(R.drawable.checkboxempty)
                genderEtc.setImageResource(R.drawable.checkboxchecked)
            }
        }
    }
    private fun getUserGender():String{
        var genderString=""
        when(genderChecked){
            0 -> {
                genderString= "none"
            }
            1->{
                genderString= "male"
            }
            2 ->{
                genderString= "female"
            }
            3->{
                genderString= "etc"
            }
        }
        return genderString
    }

    override fun errorInInfo(errorField: String) {
        makeToast("error is "+errorField)
        errorMentView.visibility = View.VISIBLE
        when(errorField){
            "username"->{
                makeToast("msg: "+errorField)
                errorMentView.setText("이미 계정이 있는 이메일 주소입니다")
                errorOccuredEditTextChangeUI(usernameView)
            }
            "password not inserted"->{
                Log.d("msg",""+"password not inserted")
                errorMentView.setText("비밀번호를 입력해주세요")
                errorOccuredEditTextChangeUI(userPasswordCheckView,userPasswordView)
            }
            "password not same"->{
                Log.d("msg",""+"password not same")
                errorMentView.setText("비밀번호가 일치하지 않습니다")
                errorOccuredEditTextChangeUI(userPasswordCheckView,userPasswordView)
            }
//            "userAge"->{
//                errorMentView.setText("나이를 입력해주세요")
//                errorOccuredEditTextChangeUI(userAge)
//            }
            "userGender"->{
                errorMentView.setText("성별을 선택해주세요")
            }
            "userNickname"->{
                errorMentView.setText("닉네임을 입력해주세요")
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }
    private fun setDividerColor(picker: NumberPicker, color: Int) //픽커 글자 컬러를 변경
    {
        val pickerFields = NumberPicker::class.java.declaredFields
        for (pf in pickerFields) {
            if (pf.name == "mSelectionDivider")
            {
                pf.isAccessible = true
                try
                {
                    val colorDrawable = ColorDrawable(color)
                    pf.set(picker, colorDrawable)
                } catch (e: IllegalArgumentException)
                {
                    e.printStackTrace()
                } catch (e: Resources.NotFoundException)
                {
                    e.printStackTrace()
                } catch (e: IllegalAccessException)
                {
                    e.printStackTrace()
                }
                break
            }
        }
    }

}