package com.dylanc.dontforget.utils

import com.dylanc.longan.AlertBuilder
import com.google.android.material.dialog.MaterialAlertDialogBuilder


var MaterialAlertDialogBuilder.title: String
  @Deprecated("Property does not have a getter", level = DeprecationLevel.ERROR)
  get() = throw RuntimeException("Property does not have a getter")
  set(value) {
    setTitle(value)
  }

fun AlertBuilder<*>.items(vararg items: CharSequence, onItemClick: (CharSequence, Int) -> Unit) =
  items(listOf(*items)) { _, which ->
    onItemClick(items[which], which)
  }