package com.example.cocoman

import android.app.Activity
import android.content.Intent
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import kotlin.reflect.KClass

class BaseContract {
    interface Presenter<in T> {
        fun attach(view: T)
        fun detach()
    }

    interface View {
        fun navigateWithFinish(clazz: KClass<out Activity>)
        fun navigateWithoutFinish(clazz: KClass<out Activity>)
        fun navigateActivityForResult(intent: Intent, code: Int)
        fun startLoading()
        fun stopLoading()
        fun makeToast(message: String?)
    }
}