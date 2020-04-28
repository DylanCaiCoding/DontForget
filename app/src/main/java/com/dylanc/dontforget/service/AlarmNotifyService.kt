package com.dylanc.dontforget.service

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.ListAdapter

import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.constant.CHANNEL_ID
import com.dylanc.dontforget.data.constant.CHANNEL_NAME
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.utilktx.logDebug

class AlarmNotifyService : Service() {

  companion object{
    var alreadyStarted = false
  }

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
    }
  }

  override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
    val dontForgetInfo = DontForgetInfoRepository.randomDontForgetInfo
      ?: return super.onStartCommand(intent, flags, startId)

    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val pendingIntent = PendingIntent.getActivity(
      this, 1,
      Intent(this, MainActivity::class.java), 0
    )

    //        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_layout_not_forget_info);
    //        remoteViews.setTextViewText(R.id.tv_msg,contentText);
    //        remoteViews.setImageViewResource(R.id.iv_logo,R.mipmap.ic_launcher);
    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
      //                .setContent(remoteViews)
      .setContentTitle(dontForgetInfo.title)
      .setContentText(dontForgetInfo.content)
      .setWhen(System.currentTimeMillis())
      .setSmallIcon(R.mipmap.ic_launcher)
      .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)
      .build()
    notification.flags = Notification.FLAG_ONGOING_EVENT
    manager.notify(1, notification)
    alreadyStarted = true
    return super.onStartCommand(intent, flags, startId)
  }

  @TargetApi(Build.VERSION_CODES.O)
  private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
    val channel = NotificationChannel(channelId, channelName, importance)
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
  }
}
