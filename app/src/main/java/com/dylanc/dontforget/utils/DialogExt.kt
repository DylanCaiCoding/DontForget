package com.dylanc.dontforget.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import com.dylanc.dontforget.BuildConfig
import com.dylanc.dontforget.data.bean.AppVersion
import com.dylanc.utilktx.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.alertItems(vararg items: CharSequence, onItemClick: (CharSequence, Int) -> Unit) =
  MaterialAlertDialogBuilder(this)
    .setItems(items) { _, which ->
      onItemClick(items[which], which)
    }
    .create()

fun Fragment.alertItems(vararg items: CharSequence, onItemClick: (CharSequence, Int) -> Unit) =
  requireActivity().alertItems(*items, onItemClick = onItemClick)

fun Fragment.alertNewVersionDialog(appVersion: AppVersion) {
  if (appVersion.version.toInt() <= BuildConfig.VERSION_CODE){
    toast("已是最新版")
    return
  }
  MaterialAlertDialogBuilder(requireContext())
    .setTitle("检查到新版本 v${appVersion.versionShort}")
    .apply {
      if (appVersion.changelog.isNullOrBlank()) setMessage(appVersion.changelog)
    }
    .setPositiveButton("更新") { _, _ ->
      requireContext().startActivity(Intent(ACTION_VIEW,Uri.parse("http://d.firim.vip/dontforget")))
    }
    .setNegativeButton("取消") { dialog, _ ->
      dialog.dismiss()
    }
    .create()
    .show()
}

