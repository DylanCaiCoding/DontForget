package com.dylanc.dontforget.utils

import android.app.Activity
import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.dylanc.utilktx.application

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
fun <T : ViewDataBinding> Activity.bindContentView(layoutId: Int): T =
  DataBindingUtil.setContentView(this, layoutId)

fun <T : ViewDataBinding> bindView(root: View): T =
  DataBindingUtil.bind<T>(root)!!

val ComponentActivity.lifecycleOwner: LifecycleOwner
  get() = this

val applicationViewModelStore = ViewModelStore()
val androidViewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)

@MainThread
inline fun <reified VM : ViewModel> applicationViewModels(): Lazy<VM> =
  ViewModelLazy(VM::class, { applicationViewModelStore }, { androidViewModelFactory })