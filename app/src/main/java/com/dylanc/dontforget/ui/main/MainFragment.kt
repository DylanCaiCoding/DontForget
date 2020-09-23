package com.dylanc.dontforget.ui.main

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.binding.setOnCheckedChangeListener
import com.dylanc.dontforget.base.event.postEventValue
import com.dylanc.dontforget.data.constant.KEY_UPDATE_INTERVALS
import com.dylanc.dontforget.data.repository.SettingRepository
import com.dylanc.dontforget.service.NotifyInfoService
import com.dylanc.dontforget.utils.alertNewVersionDialog
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.utils.requestViewModels
import com.dylanc.dontforget.viewmodel.event.SharedViewModel
import com.dylanc.dontforget.viewmodel.request.LoginRequestViewModel
import com.dylanc.dontforget.viewmodel.request.VersionRequestViewModel
import com.dylanc.utilktx.putSpValue
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.menu_item_switch.view.*

@AndroidEntryPoint
class MainFragment : Fragment() {

  private val viewModel: MainViewModel by viewModels()
  private val loginRequestViewModel: LoginRequestViewModel by requestViewModels()
  private val versionRequestViewModel: VersionRequestViewModel by requestViewModels()
  private val sharedViewModel: SharedViewModel by activityViewModels()
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var notifyInfoService: NotifyInfoService
  private var bound = false

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.fragment_main, container, false)
      .apply { bindView(this, viewModel) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val navController = requireActivity().findNavController(R.id.fragment_main)
    appBarConfiguration = AppBarConfiguration(setOf(R.id.infoListFragment), drawer_layout)
    toolbar.setupWithNavController(navController, appBarConfiguration)
    toolbar.title = "搜索"
    toolbar.inflateMenu(R.menu.main)
    toolbar.setOnMenuItemClickListener(clickProxy::onOptionsItemSelected)

//    nav_view.getHeaderView(0).tv_header.addMarginTopEqualStatusBarHeight()
//    nav_view.setupWithNavController(navController)
    nav_view.setNavigationItemSelectedListener(clickProxy::onNavItemSelected)
    val switchNotification = nav_view.menu.findItem(R.id.nav_notification).actionView.switch_drawer
    switchNotification.isChecked = SettingRepository.isShowNotification
    switchNotification.setOnCheckedChangeListener(eventHandler::onNotificationSwitchChecked)
    val switchNightMode = nav_view.menu.findItem(R.id.nav_night_mode).actionView.switch_drawer
    switchNightMode.isChecked = SettingRepository.isNightMode
    switchNightMode.setOnCheckedChangeListener(eventHandler::onNightModeSwitchChecked)
  }

  override fun onStart() {
    super.onStart()
    Intent(requireContext(), NotifyInfoService::class.java).also { intent ->
      requireActivity().bindService(intent, connection, BIND_AUTO_CREATE)
    }
  }

  override fun onStop() {
    super.onStop()
    requireActivity().unbindService(connection)
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
          onCheckBtnClick()
          drawer_layout.closeDrawers()
        }
        R.id.nav_intervals -> {
          onIntervalsBtnClick()
        }
      }
      false
    }

    private fun onCheckBtnClick() {
      versionRequestViewModel.checkVersion()
        .observe(viewLifecycleOwner) { appVersion ->
          alertNewVersionDialog(appVersion)
        }
    }

    private fun onIntervalsBtnClick() {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle("请选择刷新的间隔时间")
        .setSingleChoiceItems(arrayOf("5 分钟", "10 分钟", "1 小时"), 0) { dialog, which ->
          when (which) {
            0 -> {
              putSpValue(KEY_UPDATE_INTERVALS, 5)
            }
            1 -> {
              putSpValue(KEY_UPDATE_INTERVALS, 10)
            }
            2 -> {
              putSpValue(KEY_UPDATE_INTERVALS, 60)
            }
            else ->
              return@setSingleChoiceItems
          }
          sharedViewModel.isShowNotification.postEventValue(false)
          sharedViewModel.isShowNotification.postEventValue(true)
          drawer_layout.closeDrawers()
          dialog.dismiss()
        }
        .create()
        .show()
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
      return when (item.itemId) {
        R.id.action_logout -> {
          onLogoutBtnClick()
          true
        }
        else -> false
      }
    }

    private fun onLogoutBtnClick() {
      loginRequestViewModel.logout()
        .observe(viewLifecycleOwner, Observer {
          findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        })
    }
  }

  inner class EventHandler {

    fun onNotificationSwitchChecked(isChecked: Boolean) {
      sharedViewModel.isShowNotification.postEventValue(isChecked)
      SettingRepository.isShowNotification = isChecked
      if (!isChecked && bound) {
        notifyInfoService.hideNotification()
      }
    }

    fun onNightModeSwitchChecked(isChecked: Boolean) {
      SettingRepository.isNightMode = isChecked
    }
  }

}
