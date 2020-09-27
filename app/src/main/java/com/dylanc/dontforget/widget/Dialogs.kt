package com.dylanc.dontforget.widget

import androidx.fragment.app.Fragment
import com.dylanc.dontforget.BuildConfig
import com.dylanc.dontforget.data.bean.AppVersion
import com.dylanc.dontforget.utils.downloadApk
import com.dylanc.dontforget.utils.materialDialog
import com.dylanc.utilktx.toast


fun Fragment.alertNewVersionDialog(appVersion: AppVersion) {
  if (appVersion.version.toInt() <= BuildConfig.VERSION_CODE) {
    toast("已是最新版")
    return
  }
  materialDialog {
    setTitle("检查到新版本 v${appVersion.versionShort}")
    if (appVersion.changelog.isNullOrBlank()) setMessage(appVersion.changelog)
    setPositiveButton("更新") { _, _ ->
      requireContext().downloadApk(appVersion.installUrl, "dontforget.apk")
    }
    setNegativeButton("取消") { dialog, _ ->
      dialog.dismiss()
    }
  }.show()
}
