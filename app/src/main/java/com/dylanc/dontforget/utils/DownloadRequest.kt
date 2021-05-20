package com.dylanc.dontforget.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import com.dylanc.longan.application
import java.io.File
import kotlin.DeprecationLevel.ERROR


const val NO_GETTER: String = "Property does not have a getter"

inline fun noGetter(): Nothing = throw NotImplementedError(com.dylanc.longan.NO_GETTER)

inline fun downloadRequest(url: String, fileName: String, block: DownloadRequestBuilder.() -> Unit) =
  DownloadRequest(url, fileName).apply(block)

interface DownloadRequestBuilder {
  val url: String
  val fileName: String

  var title: CharSequence
    @Deprecated(NO_GETTER, level = ERROR) get

  var description: CharSequence
    @Deprecated(NO_GETTER, level = ERROR) get

  var mimeType: String
    @Deprecated(NO_GETTER, level = ERROR) get

  var allowedNetworkTypes: Int
    @Deprecated(NO_GETTER, level = ERROR) get

  var notificationVisibility: Int
    @Deprecated(NO_GETTER, level = ERROR) get

  var allowedOverRoaming: Boolean
    @Deprecated(NO_GETTER, level = ERROR) get

//  fun destinationInExternalFilesDir(dirType: String, subPath: String)
//  fun destinationInExternalPublicDir(dirType: String, subPath: String)

  fun download(onComplete: (File) -> Unit): BroadcastReceiver
}

class DownloadRequest(override val url: String, override val fileName: String) : DownloadRequestBuilder {
  private val request = DownloadManager.Request(Uri.parse(url))

  override var title: CharSequence
    @Deprecated(NO_GETTER, level = ERROR)
    get() = noGetter()
    set(value) {
      request.setTitle(value)
    }

  override var description: CharSequence
    @Deprecated(NO_GETTER, level = ERROR)
    get() = noGetter()
    set(value) {
      request.setDescription(value)
    }

  override var mimeType: String
    @Deprecated(NO_GETTER, level = ERROR)
    get() = noGetter()
    set(value) {
      request.setMimeType(value)
    }

  override var allowedNetworkTypes: Int
    @Deprecated(NO_GETTER, level = ERROR)
    get() = noGetter()
    set(value) {
      request.setAllowedNetworkTypes(value)
    }

  override var notificationVisibility: Int
    @Deprecated(NO_GETTER, level = ERROR)
    get() = noGetter()
    set(value) {
      request.setNotificationVisibility(value)
    }

  override var allowedOverRoaming: Boolean
    @Deprecated(NO_GETTER, level = ERROR)
    get() = noGetter()
    set(value) {
      request.setAllowedOverRoaming(value)
    }

//  override fun destinationInExternalFilesDir(dirType: String, subPath: String) {
//    request.setDestinationInExternalFilesDir(application, dirType, subPath)
//  }

  override fun download(onComplete: (File) -> Unit): BroadcastReceiver {
    request.setDestinationInExternalFilesDir(application, Environment.DIRECTORY_DOWNLOADS, fileName)
    val downloadManager = application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val downloadId = downloadManager.enqueue(request)
    val downloadCompleteReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        if (id == downloadId) {
          downloadManager.query(DownloadManager.Query().setFilterById(downloadId))?.use {
//            val uri = it.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
//            contentResolver.openFileDescriptor(uri,"r")
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
            onComplete(file)
          }
        }
      }
    }
    application.registerReceiver(downloadCompleteReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    return downloadCompleteReceiver
  }

}