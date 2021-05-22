package com.dylanc.dontforget.widget

import androidx.fragment.app.Fragment
import com.dylanc.dontforget.BuildConfig
import com.dylanc.dontforget.data.bean.AppVersion
import com.dylanc.dontforget.utils.downloadApk
import com.dylanc.longan.alertDialog
import com.dylanc.longan.toast

fun Fragment.alertNewVersionDialog(appVersion: AppVersion) {
  if (appVersion.version.toInt() <= BuildConfig.VERSION_CODE) {
    toast("已是最新版")
    return
  }
  alertDialog {
    title = "检查到新版本 v${appVersion.versionShort}"
    if (!appVersion.changelog.isNullOrBlank()) message = appVersion.changelog
    positiveButton("更新") {
      requireContext().downloadApk(appVersion.installUrl, "dontforget.apk")
//      downloadRequest(appVersion.installUrl) {
//        title = "正在下载"
//        allowedNetworkTypes = DownloadManager.Request.NETWORK_WIFI
//        notificationVisibility = DownloadManager.Request.VISIBILITY_VISIBLE
//        allowedOverRoaming = false
//        destinationInExternalFilesDir(Environment.DIRECTORY_DOWNLOADS, "dontforget.apk")
//      }.download {
//        startActivity(installAPKIntentOf(it))
//      }
    }
    negativeButton("取消") {
      it.dismiss()
    }
  }.show()
}
