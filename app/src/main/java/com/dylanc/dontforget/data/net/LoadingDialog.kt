package com.dylanc.dontforget.data.net

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dylanc.dontforget.R

class LoadingDialog : DialogFragment() {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      Dialog(it, R.style.DialogTheme).apply {
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      }
    } ?: throw IllegalStateException("Activity cannot be null")
  }

  fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG_LOADING)

  override fun dismiss() {
    if (isAdded) {
      super.dismiss()
    }
  }

  companion object {
    private const val TAG_LOADING = "loading"
  }
}