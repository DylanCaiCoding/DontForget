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
import java.io.File

fun Context.downloadApk(url: String, fileName: String) {
  val request = DownloadManager.Request(Uri.parse(url))
    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
    .setTitle("正在下载")
    .setAllowedOverRoaming(false)
    .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, fileName)
  val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
  val downloadId = downloadManager.enqueue(request)
  registerReceiver(DownloadCompleteReceiver(downloadId), IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
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
