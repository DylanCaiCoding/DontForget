package com.dylanc.dontforget.utils

import android.app.Activity
import android.view.View
import androidx.core.app.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
fun <T : ViewDataBinding> Activity.bindContentView(layoutId: Int): T =
  DataBindingUtil.setContentView(this, layoutId)

fun <T : ViewDataBinding> bindView(root: View) =
  DataBindingUtil.bind<T>(root)!!

val ComponentActivity.lifecycleOwner: LifecycleOwner
  get() = this

