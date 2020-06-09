package com.dylanc.dontforget.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.repository.infoRepository
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.utilktx.intentOf

private const val CHANNEL_ID = "not_forget"
private const val CHANNEL_NAME = "勿忘消息"

class NotifyService : Service() {

  private val binder = NotifyBinder()

  companion object {
    var alreadyStarted = false
  }

  override fun onBind(intent: Intent): IBinder? {
    return binder
  }

  override fun onCreate() {
    super.onCreate()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
      val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    showNotification()
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onDestroy() {
    super.onDestroy()
    hideNotification()
  }

  private fun showNotification() {
    val info = infoRepository.randomInfo ?: return
    val pendingIntent = PendingIntent.getActivity(this, 1, intentOf<MainActivity>(), 0)
    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentText(info.title)
      .setStyle(
        NotificationCompat.BigTextStyle()
          .bigText(info.title)
      )
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentIntent(pendingIntent)
      .build()
    notification.flags = Notification.FLAG_ONGOING_EVENT
    startForeground(1, notification)
    alreadyStarted = true
  }

  fun hideNotification() {
    alreadyStarted = false
    stopForeground(true)
  }

  inner class NotifyBinder : Binder() {
    val service: NotifyService
      get() = this@NotifyService
  }

}
