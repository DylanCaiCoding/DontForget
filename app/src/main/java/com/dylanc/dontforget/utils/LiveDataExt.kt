@file:Suppress("NOTHING_TO_INLINE")

package com.dylanc.dontforget.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kunminx.architecture.ui.callback.UnPeekLiveData

@MainThread
inline fun <T :List<*>> LiveData<T>.exceptFirstEmpty(): LiveData<T?> {
  var isFirst = true
  return map {
    if (isFirst) {
      isFirst = false
      if (it.size == 0) null else it
    } else {
      it
    }
  }
}