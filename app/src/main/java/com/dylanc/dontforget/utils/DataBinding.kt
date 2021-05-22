@file:Suppress("unused")

package com.dylanc.dontforget.utils

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */

fun <T : ViewDataBinding> T.bind(viewModel: ViewModel, vararg bindingParams: Pair<Int, Any>) =
  apply {
    setVariable(BR.viewModel, viewModel)
    for (bindingParam in bindingParams) {
      setVariable(bindingParam.first, bindingParam.second)
    }
  }