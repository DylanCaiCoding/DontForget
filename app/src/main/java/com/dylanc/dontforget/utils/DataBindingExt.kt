@file:Suppress("unused")

package com.dylanc.dontforget.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

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

fun Fragment.bindView(
  root: View, viewModel: ViewModel,
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

fun Fragment.bindView(
  inflater: LayoutInflater, layoutId: Int, container: ViewGroup?,
  viewModel: ViewModel, vararg bindingParams: Pair<Int, Any>
): ViewDataBinding =
  DataBindingUtil.bind<ViewDataBinding>(inflater.inflate(layoutId, container, false))!!
    .apply {
      setVariable(BR.viewModel, viewModel)
      lifecycleOwner = this@bindView
      for (bindingParam in bindingParams) {
        setVariable(bindingParam.first, bindingParam.second)
      }
    }
