package com.dylanc.dontforget.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.alertItems(vararg items: CharSequence, onItemClick: (CharSequence, Int) -> Unit) =
  MaterialAlertDialogBuilder(this)
    .setItems(items) { _, which ->
      onItemClick(items[which], which)
    }
    .create()

fun Fragment.alertItems(vararg items: CharSequence, onItemClick: (CharSequence, Int) -> Unit) =
  requireActivity().alertItems(*items, onItemClick = onItemClick)
