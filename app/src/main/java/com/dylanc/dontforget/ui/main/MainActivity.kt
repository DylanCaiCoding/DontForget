package com.dylanc.dontforget.ui.main

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.BarUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.item.DontForgetInfoDelegate
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_ADD_INFO
import com.dylanc.dontforget.data.constant.REQUEST_CODE_ADD_INFO
import com.dylanc.dontforget.data.constant.REQUEST_CODE_ALARM_NOTIFY
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.databinding.ActivityMainBinding
import com.dylanc.dontforget.service.AlarmNotifyService
import com.dylanc.dontforget.ui.main.add_info.AddInfoActivity
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.startActivityForResult
import com.dylanc.utilktx.toast
import kotlinx.android.synthetic.main.common_layout_toolbar.*
import java.util.*

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val adapter = MultiTypeAdapter()

  private lateinit var mainViewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = setBindingContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    BarUtils.setStatusBarLightMode(this, true)
    adapter.register(DontForgetInfoDelegate())
    mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    binding.viewModel = mainViewModel
    binding.adapter = adapter
    binding.lifecycleOwner = this

    mainViewModel.requestList()
    mainViewModel.list.observe(this, androidx.lifecycle.Observer {
      startNotifyAlarm()
    })
  }

  private fun startNotifyAlarm() {
    if (DontForgetInfoRepository.infos.isEmpty()) {
      return
    }
    val intent = Intent(this, AlarmNotifyService::class.java)
    val pendingIntent = PendingIntent.getService(
      this, REQUEST_CODE_ALARM_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerAtMillis = Date().time
    val intervalMillis = 60 * 2000
    alarmManager.setRepeating(
      AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis.toLong(), pendingIntent
    )
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
            adapter.notifyDataSetChanged()
          }
        }
        true
      }
      R.id.action_edit -> {
        toast("编辑")
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
