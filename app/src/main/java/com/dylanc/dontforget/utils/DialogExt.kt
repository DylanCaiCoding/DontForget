package com.dylanc.dontforget.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.dylanc.dontforget.BuildConfig
import com.dylanc.dontforget.data.bean.AppVersion
import com.dylanc.utilktx.installApp
import com.dylanc.utilktx.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

fun Context.alertItems(vararg items: CharSequence, onItemClick: (CharSequence, Int) -> Unit) =
  MaterialAlertDialogBuilder(this)
    .setItems(items) { _, which ->
      onItemClick(items[which], which)
    }
    .create()

fun Fragment.alertItems(vararg items: CharSequence, onItemClick: (CharSequence, Int) -> Unit) =
  requireActivity().alertItems(*items, onItemClick = onItemClick)

fun Fragment.alertNewVersionDialog(appVersion: AppVersion) {
  if (appVersion.version.toInt() <= BuildConfig.VERSION_CODE) {
    toast("已是最新版")
    return
  }
  val context = requireContext()
  MaterialAlertDialogBuilder(context)
    .setTitle("检查到新版本 v${appVersion.versionShort}")
    .apply {
      if (appVersion.changelog.isNullOrBlank()) setMessage(appVersion.changelog)
    }
    .setPositiveButton("更新") { _, _ ->
      val request = DownloadManager.Request(Uri.parse(appVersion.installUrl))
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        .setTitle("勿忘")
        .setDescription("正在下载新版本")
        .setAllowedOverRoaming(false)
        .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "dontforget.apk")
      val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
      val downloadId = downloadManager.enqueue(request)
      context.registerReceiver(DownloadCompleteReceiver(downloadId), IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
    .setNegativeButton("取消") { dialog, _ ->
      dialog.dismiss()
    }
    .create()
    .show()
}

class DownloadCompleteReceiver(private val downloadId: Long) : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
    if (id == downloadId) {
      val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
      downloadManager.query(DownloadManager.Query().setFilterById(downloadId))?.let {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "dontforget.apk")
        context.installAPK(file)
      }
    }
  }
}

fun Context.installAPK(file: File) {
  val intent = Intent(Intent.ACTION_VIEW)
  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
  if (Build.VERSION.SDK_INT >= 24) {
    val apkUri = FileProvider.getUriForFile(this, "$packageName.fileProvider", file)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
  } else {
    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
  }
  startActivity(intent)
}
