package com.dylanc.dontforget.data.net

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.dylanc.dontforget.R

class LoadingDialog(var fragmentActivity: FragmentActivity? = null) : DialogFragment() {
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
    if (isShow){
      fragmentActivity?.supportFragmentManager?.let { show(it, TAG_LOADING) }
    }else if (isAdded){
      dismiss()
    }
  }

  companion object {
    private const val TAG_LOADING = "loading"
  }
}