package com.dylanc.dontforget.utils

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
fun <T : ViewDataBinding> Activity.setBindingContentView(layoutId: Int): T =
  DataBindingUtil.setContentView(this, layoutId)

inline fun <reified T : ViewModel> ComponentActivity.viewModelOf() =
  ViewModelProvider(this).get(T::class.java)

