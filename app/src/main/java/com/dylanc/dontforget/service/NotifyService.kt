package com.dylanc.dontforget.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.constant.CHANNEL_ID
import com.dylanc.dontforget.data.constant.CHANNEL_NAME
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.utilktx.intentOf

class NotifyService : Service() {

  companion object {
    var alreadyStarted = false
  }

  private val binder = NotifyBinder()

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
    if (showNotification()) return super.onStartCommand(intent, flags, startId)
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onDestroy() {
    super.onDestroy()
    hideNotification()
  }

  fun showNotification(): Boolean {
    val dontForgetInfo = DontForgetInfoRepository.randomDontForgetInfo
      ?: return true

    val pendingIntent = PendingIntent.getActivity(this, 1, intentOf<MainActivity>(), 0)
    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentText(dontForgetInfo.title)
      .setStyle(
        NotificationCompat.BigTextStyle()
          .bigText(dontForgetInfo.title)
      )
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentIntent(pendingIntent)
      .build()
    notification.flags = Notification.FLAG_ONGOING_EVENT
    startForeground(1, notification)
    alreadyStarted = true
    return false
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
