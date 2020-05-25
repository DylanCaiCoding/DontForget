package com.dylanc.dontforget.data.net

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.dylanc.dontforget.R
import com.dylanc.retrofit.helper.rxjava.RequestLoading
import com.dylanc.retrofit.helper.rxjava.showLoading
import io.reactivex.Single

private const val TAG_LOADING = "loading"

fun <T> Single<T>.showLoadingDialog(activity: FragmentActivity): Single<T> =
  showLoading(RxLoadingDialog(activity))

class RxLoadingDialog(private val activity: FragmentActivity) : RequestLoading {

  private var dialog = LoadingDialog()

  override fun show() {
    dialog.show(activity.supportFragmentManager, TAG_LOADING)
  }

  override fun dismiss() {
    dialog.dismiss()
  }
}

class LoadingDialog : DialogFragment() {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      Dialog(it, R.style.DialogTheme).apply {
        setContentView(R.layout.dialog_loading)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      }
    } ?: throw IllegalStateException("Activity cannot be null")
  }
}