package com.dylanc.dontforget.data.net

import android.app.Dialog
import android.content.Context
import com.dylanc.dontforget.widget.LoadingDialog
import com.dylanc.retrofit.helper.RequestLoading

class RxLoadingDialog(private val context: Context) : RequestLoading {

  private var dialog: Dialog? = null

  override fun show() {
    dialog = LoadingDialog(context)
    dialog!!.show()
  }

  override fun dismiss() {
    dialog?.dismiss()
  }
}