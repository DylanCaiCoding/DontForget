package com.dylanc.dontforget.data.net

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.RequestLoading

class LoadingDialog(private val fragmentActivity: FragmentActivity) : DialogFragment(), RequestLoading {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      Dialog(it, R.style.DialogTheme).apply {
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      }
    } ?: throw IllegalStateException("Activity cannot be null")
  }

  override fun show(isLoading: Boolean) {
    if (isLoading) {
      show(fragmentActivity.supportFragmentManager, TAG_LOADING)
    } else {
      dismiss()
    }
  }

  companion object {
    private const val TAG_LOADING = "loading"
  }
}