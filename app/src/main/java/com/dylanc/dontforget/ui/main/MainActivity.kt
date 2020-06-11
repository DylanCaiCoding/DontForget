package com.dylanc.dontforget.ui.main

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.binding.setOnCheckedChangeListener
import com.dylanc.dontforget.data.constant.EVENT_NOTIFICATION
import com.dylanc.dontforget.data.constant.KEY_SHOW_NOTIFICATION
import com.dylanc.dontforget.data.constant.KEY_UPDATE_INTERVALS
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.data.net.Status
import com.dylanc.dontforget.databinding.ActivityMainBinding
import com.dylanc.dontforget.service.NotifyInfoService
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.dontforget.utils.bindContentView
import com.dylanc.dontforget.utils.lifecycleOwner
import com.dylanc.dontforget.view_model.request.UserRequestViewModel
import com.dylanc.dontforget.view_model.request.VersionRequestViewModel
import com.dylanc.liveeventbus.observeEvent
import com.dylanc.liveeventbus.postEvent
import com.dylanc.utilktx.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.menu_item_switch.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import update.UpdateAppUtils


class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()
  private val userRequestViewModel: UserRequestViewModel by viewModels()
  private val versionRequestViewModel: VersionRequestViewModel by viewModels()
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var notifyInfoService: NotifyInfoService
  private val loadingDialog = LoadingDialog()
  private var bound = false
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = bindContentView(R.layout.activity_main)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
    setStatusBarLightMode(true)
    setSupportActionBar(toolbar)
    toolbar.title = "搜索"
    val navController = findNavController(R.id.nav_host_fragment)
    appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawer_layout)
    setupActionBarWithNavController(navController, appBarConfiguration)
    nav_view.getHeaderView(0).tv_header.addMarginTopEqualStatusBarHeight()
//    nav_view.setupWithNavController(navController)
    nav_view.setNavigationItemSelectedListener(clickProxy::onNavItemSelected)
    val switchNotification = nav_view.menu.findItem(R.id.nav_notification).actionView.switch_drawer
    switchNotification.isChecked = spValueOf(KEY_SHOW_NOTIFICATION, true)
    switchNotification.setOnCheckedChangeListener(clickProxy::onNotifySwitchChecked)
    eventHandler.observe()

//    userRequestViewModel.user.observe(this, Observer { user ->
//      if (user == null) {
//        loadingDialog.dismiss()
//        startActivity<LoginActivity>()
//        finish()
//      }
//    })
    versionRequestViewModel.appVersion.observe(this, Observer { appVersion ->
      UpdateAppUtils
        .getInstance()
        .apkUrl(appVersion.installUrl)
        .updateTitle("检查到新版本 v${appVersion.versionShort}")
        .update()
    })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return clickProxy.onOptionsItemSelected(item)
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  override fun onStart() {
    super.onStart()
    Intent(this, NotifyInfoService::class.java).also { intent ->
      bindService(intent, connection, BIND_AUTO_CREATE)
    }
  }

  override fun onStop() {
    super.onStop()
    unbindService(connection)
  }

  private val connection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
      val binder = service as NotifyInfoService.NotifyBinder
      notifyInfoService = binder.service
      bound = true
    }

    override fun onServiceDisconnected(name: ComponentName) {
      bound = false
    }
  }

  inner class ClickProxy {

    fun onNavItemSelected(menuItem: MenuItem) = run {
      when (menuItem.itemId) {
        R.id.nav_check_update -> {
          versionRequestViewModel.checkVersion()
          drawer_layout.closeDrawers()
        }
        R.id.nav_intervals -> {
          MaterialAlertDialogBuilder(this@MainActivity)
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
      }
      false
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
      return when (item.itemId) {
        R.id.action_logout -> {
          userRequestViewModel.logout()
            .observe(lifecycleOwner, Observer {
              when (it.status) {
                Status.LOADING -> loadingDialog.show(supportFragmentManager)
                Status.SUCCESS -> {
                  loadingDialog.dismiss()
                  startActivity<LoginActivity>()
                  finish()
                }
                Status.ERROR -> {
                  loadingDialog.dismiss()
                  toast(it.message)
                }
              }
            })
          true
        }
        else -> {
          false
        }
      }
    }

    fun onNotifySwitchChecked(isChecked: Boolean) {
      putSP(KEY_SHOW_NOTIFICATION, isChecked)
      postEvent(EVENT_NOTIFICATION, isChecked)
    }
  }

  inner class EventHandler {

    fun observe() {
      observeEvent(EVENT_NOTIFICATION, this::onNotificationEvent)
    }

    private fun onNotificationEvent(isChecked: Boolean) {
      if (!isChecked && bound) {
        notifyInfoService.hideNotification()
      }
    }
  }

}
