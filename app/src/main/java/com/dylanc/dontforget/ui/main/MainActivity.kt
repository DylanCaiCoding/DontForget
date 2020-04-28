package com.dylanc.dontforget.ui.main

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.recycler.DontForgetInfoAdapter
import com.dylanc.dontforget.adapter.recycler.DontForgetInfoDelegate
import com.dylanc.dontforget.data.api.VersionApi
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_INFO
import com.dylanc.dontforget.data.constant.REQUEST_CODE_ADD_INFO
import com.dylanc.dontforget.data.constant.REQUEST_CODE_ALARM_NOTIFY
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.data.repository.db.infoDb
import com.dylanc.dontforget.databinding.ActivityMainBinding
import com.dylanc.dontforget.service.AlarmNotifyService
import com.dylanc.dontforget.ui.main.add_info.AddInfoActivity
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.startActivityForResult
import com.dylanc.utilktx.toast
import kotlinx.android.synthetic.main.layout_toolbar.*
import update.UpdateAppUtils
import java.util.*


class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  //  private val adapter = MultiTypeAdapter()
  private val adapter = DontForgetInfoAdapter()

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = setBindingContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    BarUtils.setStatusBarLightMode(this, true)
//    adapter.register(DontForgetInfoDelegate())
    binding.viewModel = viewModel
    binding.adapter = adapter
    binding.lifecycleOwner = this

    viewModel.requestList(this)
    viewModel.list.observe(this, androidx.lifecycle.Observer {
      startNotifyAlarm()
    })
  }

  private fun startNotifyAlarm() {
    if (AlarmNotifyService.alreadyStarted || DontForgetInfoRepository.infos.isEmpty()) {
      return
    }

    val intent = Intent(this, AlarmNotifyService::class.java)
    val pendingIntent = PendingIntent.getService(
      this, REQUEST_CODE_ALARM_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerAtMillis = Date().time
    val intervalMillis = 60 * 1000
    alarmManager.setRepeating(
      AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis.toLong(), pendingIntent
    )
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//      alarmManager.setExactAndAllowWhileIdle(
//        AlarmManager.ELAPSED_REALTIME_WAKEUP,
//        SystemClock.elapsedRealtime(),
//        pendingIntent
//      )
//    } else {
//      alarmManager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent)
//    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_add -> {
        startActivityForResult<AddInfoActivity>(REQUEST_CODE_ADD_INFO) { resultCode, data ->
          if (resultCode == Activity.RESULT_OK) {
            val newInfo = data!!.getParcelableExtra(KEY_INFO) as DontForgetInfo
            DontForgetInfoRepository.addInfo(newInfo)
            val list = viewModel.list.value!!
            if (list.isNotEmpty()) {
              for (i in list.indices) {
                val info = list[i] as DontForgetInfo
                if (newInfo.dateStr != info.dateStr || i == list.size - 1) {
                  list.add(i, newInfo)
                  break
                }
              }
            } else {
              list.add(newInfo)
            }
            viewModel.list.value = list
          }
        }
        true
      }
      R.id.action_edit -> {
        toast("编辑")
        true
      }
      R.id.action_check_update -> {
        apiServiceOf<VersionApi>()
          .check()
          .io2mainThread()
          .subscribe({ appVersion ->
            UpdateAppUtils
              .getInstance()
              .apkUrl(appVersion.installUrl)
              .updateTitle("检查到新版本 v${appVersion.versionShort}")
              .update()
          }, {})
        true
      }
      R.id.action_logout -> {
        UserRepository.logout()
        startActivity<LoginActivity>()
        finish()
        true
      }
      else -> {
        super.onOptionsItemSelected(item)
      }
    }
  }
}
