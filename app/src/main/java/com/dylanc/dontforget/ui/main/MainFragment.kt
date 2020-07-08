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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.binding.setOnCheckedChangeListener
import com.dylanc.dontforget.base.event.postEventValue
import com.dylanc.dontforget.data.constant.KEY_UPDATE_INTERVALS
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.data.repository.SettingRepository
import com.dylanc.dontforget.service.NotifyInfoService
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.dontforget.utils.*
import com.dylanc.dontforget.view_model.event.SharedViewModel
import com.dylanc.dontforget.view_model.request.UserRequestViewModel
import com.dylanc.dontforget.view_model.request.VersionRequestViewModel
import com.dylanc.retrofit.helper.coroutines.RequestException
import com.dylanc.utilktx.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.menu_item_switch.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import update.UpdateAppUtils


class MainFragment : Fragment() {

  private val viewModel: MainViewModel by viewModels()
  private val userRequestViewModel: UserRequestViewModel by viewModels()
  private val versionRequestViewModel: VersionRequestViewModel by viewModels()
  private val sharedViewModel: SharedViewModel by applicationViewModels()
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var notifyInfoService: NotifyInfoService
  private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
  private var bound = false

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindView(view, viewModel)
    initNavigationView()
    eventHandler.observe()
  }

  private fun initNavigationView() {
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
      loadingDialog.show(true)
      versionRequestViewModel.checkVersion()
        .observe(lifecycleOwner, Observer { appVersion ->
          loadingDialog.show(false)
          UpdateAppUtils
            .getInstance()
            .apkUrl(appVersion.installUrl)
            .updateTitle("检查到新版本 v${appVersion.versionShort}")
            .update()
        })
    }

    private fun onIntervalsBtnClick() {
      MaterialAlertDialogBuilder(requireContext())
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
      loadingDialog.show(true)
      userRequestViewModel.logout()
        .observe(lifecycleOwner, Observer {
          loadingDialog.show(false)
          findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        })
    }
  }

  inner class EventHandler {
    fun observe() {
      userRequestViewModel.requestException
        .observeException(lifecycleOwner, this::onRequestException)
      versionRequestViewModel.requestException
        .observeException(lifecycleOwner, this::onRequestException)
    }

    private fun onRequestException(e: RequestException) {
      loadingDialog.show(false)
      toast(e.message)
    }

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
