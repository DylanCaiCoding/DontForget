@file:Suppress("unused")

package com.dylanc.dontforget.utils

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
fun ComponentActivity.bindContentView(
  layoutId: Int, viewModel: ViewModel,
  vararg bindingParams: Pair<Int, Any>
): ViewDataBinding =
  DataBindingUtil.setContentView<ViewDataBinding>(this, layoutId)
    .apply {
      setVariable(BR.viewModel, viewModel)
      lifecycleOwner = this@bindContentView
      for (bindingParam in bindingParams) {
        setVariable(bindingParam.first, bindingParam.second)
      }
    }

fun View.bind(owner: LifecycleOwner, viewModel: ViewModel, vararg bindingParams: Pair<Int, Any>): View =
  DataBindingUtil.bind<ViewDataBinding>(this)!!.bind(owner, viewModel, *bindingParams).root

fun <T : ViewDataBinding> T.bind(owner: LifecycleOwner, viewModel: ViewModel, vararg bindingParams: Pair<Int, Any>) =
  apply {
    setVariable(BR.viewModel, viewModel)
    for (bindingParam in bindingParams) {
      setVariable(bindingParam.first, bindingParam.second)
    }
    lifecycleOwner = owner
  }

inline fun <reified T : ViewBinding> Fragment.lazyInflate(): Lazy<T> = lazy {
  inflateBinding(layoutInflater)
}

inline fun <reified T : ViewBinding> Activity.lazyInflate(): Lazy<T> = lazy {
  inflateBinding(layoutInflater)
}

inline fun <reified T : ViewBinding> Dialog.lazyInflate(): Lazy<T> = lazy {
  inflateBinding(layoutInflater)
}

inline fun <reified T : ViewBinding> inflateBinding(layoutInflater: LayoutInflater): T {
  val method = T::class.java.getMethod("inflate", LayoutInflater::class.java)
  return method.invoke(null, layoutInflater) as T
}