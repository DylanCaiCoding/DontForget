package com.dylanc.dontforget.utils

import android.app.Activity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
fun <T : ViewDataBinding> Activity.setBindingContentView(layoutId: Int): T =
  DataBindingUtil.setContentView(this, layoutId)

