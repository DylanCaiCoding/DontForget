package com.dylanc.dontforget.widget

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import com.dylanc.dontforget.R

class LoadingDialog(context: Context, themeResId: Int = R.style.DialogTheme) :
  Dialog(context, themeResId) {
  init {
    setContentView(R.layout.dialog_loading)
    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  }
}