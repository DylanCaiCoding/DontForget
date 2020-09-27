package com.dylanc.dontforget.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.dylanc.dontforget.BuildConfig
import com.dylanc.dontforget.data.bean.AppVersion
import com.dylanc.utilktx.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.RuntimeException

fun Context.materialDialog(block: MaterialAlertDialogBuilder.() -> Unit) =
  MaterialAlertDialogBuilder(this)
    .apply(block)
    .create()

fun Fragment.materialDialog(block: MaterialAlertDialogBuilder.() -> Unit) =
  requireActivity().materialDialog(block)

var MaterialAlertDialogBuilder.title: String
  @Deprecated("Property does not have a getter", level = DeprecationLevel.ERROR)
  get() = throw RuntimeException("Property does not have a getter")
  set(value) {
    setTitle(value)
  }

fun MaterialAlertDialogBuilder.items(vararg items: CharSequence, onItemClick: (CharSequence, Int) -> Unit) =
  setItems(items) { _, which ->
    onItemClick(items[which], which)
  }