package com.dylanc.dontforget.data.net

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dylanc.dontforget.R

fun Fragment.loadingDialog() = lazy {
  LoadingDialog(requireActivity())
}

fun FragmentActivity.loadingDialog() = lazy {
  LoadingDialog(this)
}

class LoadingDialog(private val fragmentActivity: FragmentActivity) : DialogFragment() {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      Dialog(it, R.style.DialogTheme).apply {
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      }
    } ?: throw IllegalStateException("Activity cannot be null")
  }

  fun show(isShow: Boolean) {
    if (isShow) {
      show(fragmentActivity.supportFragmentManager, TAG_LOADING)
    } else {
      dismiss()
    }
  }

  companion object {
    private const val TAG_LOADING = "loading"
  }
}