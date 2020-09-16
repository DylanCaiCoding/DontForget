package com.dylanc.dontforget.utils

import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.dylanc.utilktx.app

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
fun ComponentActivity.bindContentView(
  layoutId: Int,
  viewModel: ViewModel,
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

fun Fragment.bindView(
  root: View,
  viewModel: ViewModel,
  vararg bindingParams: Pair<Int, Any>
): ViewDataBinding =
  DataBindingUtil.bind<ViewDataBinding>(root)!!
    .apply {
      setVariable(BR.viewModel, viewModel)
      lifecycleOwner = this@bindView
      for (bindingParam in bindingParams) {
        setVariable(bindingParam.first, bindingParam.second)
      }
    }

@MainThread
inline fun <reified VM : ViewModel> appViewModels(): Lazy<VM> =
  ViewModelLazy(VM::class, { ViewModelStore() }, { ViewModelProvider.AndroidViewModelFactory.getInstance(app) })