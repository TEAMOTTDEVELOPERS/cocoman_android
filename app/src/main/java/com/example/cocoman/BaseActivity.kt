package com.example.cocoman

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
    override fun navigateWithFinish(clazz: KClass<out Activity>) {
        navigateWithoutFinish(clazz)
        finish()
    }

    override fun navigateWithoutFinish(clazz: KClass<out Activity>) {
        startActivity(Intent(this, clazz.java))
    }

    override fun navigateActivityForResult(intent: Intent, code: Int) {
        startActivityForResult(intent, code)
    }

    override fun startLoading() {
        Toast.makeText(this, "start Loading", Toast.LENGTH_SHORT).show()
    }

    override fun stopLoading() {
        Toast.makeText(this, "finish Loading", Toast.LENGTH_SHORT).show()
    }

    override fun makeToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}