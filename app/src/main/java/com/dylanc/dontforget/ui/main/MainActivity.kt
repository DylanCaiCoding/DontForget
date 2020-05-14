package com.dylanc.dontforget.ui.main

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.blankj.utilcode.util.BarUtils
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.api.VersionApi
import com.dylanc.dontforget.data.constant.EVENT_NOTIFICATION
import com.dylanc.dontforget.data.constant.KEY_SHOW_NOTIFICATION
import com.dylanc.dontforget.data.constant.KEY_UPDATE_INTERVALS
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.databinding.ActivityMainBinding
import com.dylanc.dontforget.service.NotifyService
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.dontforget.utils.observeEvent
import com.dylanc.dontforget.utils.postEvent
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread
import com.dylanc.utilktx.putSP
import com.dylanc.utilktx.spValueOf
import com.dylanc.utilktx.startActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import update.UpdateAppUtils


class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var notifyService: NotifyService
  private var bound = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = setBindingContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    BarUtils.setStatusBarLightMode(this, true)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    val navController = findNavController(R.id.nav_host_fragment)
    appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawer_layout)
    setupActionBarWithNavController(navController, appBarConfiguration)
    val tvHeader: TextView = nav_view.getHeaderView(0).findViewById(R.id.tv_header)
    BarUtils.addMarginTopEqualStatusBarHeight(tvHeader)
//    nav_view.setupWithNavController(navController)
    toolbar.title = "搜索"

    nav_view.setNavigationItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.nav_check_update -> {
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
          drawer_layout.closeDrawers()
        }
        R.id.nav_intervals -> {
          MaterialAlertDialogBuilder(this)
            .setTitle("请选择刷新的间隔时间")
            .setSingleChoiceItems(arrayOf("5 分钟", "10 分钟", "1 小时"), 0) { dialog, which ->
              when (which) {
                0 -> {
                  putSP(KEY_UPDATE_INTERVALS, 5)
                }
                1 -> {
                  putSP(KEY_UPDATE_INTERVALS, 10)
                }
                2 -> {
                  putSP(KEY_UPDATE_INTERVALS, 60)
                }
                else ->
                  return@setSingleChoiceItems
              }
              dialog.dismiss()
              postEvent(EVENT_NOTIFICATION, false)
              postEvent(EVENT_NOTIFICATION, true)
              drawer_layout.closeDrawers()
            }
            .create()
            .show()
        }
        else -> {
        }
      }
      false
    }
    val switchNotification =
      nav_view.menu.findItem(R.id.nav_notify).actionView.findViewById<Switch>(R.id.switch1)
    switchNotification.isChecked = spValueOf(KEY_SHOW_NOTIFICATION, true)
    switchNotification.setOnCheckedChangeListener { _, isChecked ->
      putSP(KEY_SHOW_NOTIFICATION, isChecked)
      postEvent(EVENT_NOTIFICATION, isChecked)
    }

    observeEvent(EVENT_NOTIFICATION, this::onNotificationEvent)
  }

  private fun onNotificationEvent(isChecked: Boolean) {
    if (!isChecked && bound) {
      notifyService.hideNotification()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
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

  override fun onStart() {
    super.onStart()
    Intent(this, NotifyService::class.java).also { intent ->
      bindService(intent, connection, BIND_AUTO_CREATE)
    }
  }

  override fun onStop() {
    super.onStop()
    unbindService(connection)
  }

  private val connection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
      val binder = service as NotifyService.NotifyBinder
      notifyService = binder.service
      bound = true
    }

    override fun onServiceDisconnected(name: ComponentName) {
      bound = false
    }
  }
}
