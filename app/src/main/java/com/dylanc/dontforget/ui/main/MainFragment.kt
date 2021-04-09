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
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.binding.setOnCheckedChangeListener
import com.dylanc.dontforget.data.constant.KEY_UPDATE_INTERVALS
import com.dylanc.dontforget.databinding.FragmentMainBinding
import com.dylanc.dontforget.service.NotifyInfoService
import com.dylanc.dontforget.utils.*
import com.dylanc.dontforget.viewmodel.request.LoginRequestViewModel
import com.dylanc.dontforget.viewmodel.request.VersionRequestViewModel
import com.dylanc.dontforget.viewmodel.shared.SharedViewModel
import com.dylanc.dontforget.widget.alertNewVersionDialog
import com.dylanc.grape.sharedPreferences
import com.dylanc.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

  private val binding: FragmentMainBinding by binding()
  private val viewModel: MainViewModel by viewModels()
  private val loginRequestViewModel: LoginRequestViewModel by requestViewModels()
  private val versionRequestViewModel: VersionRequestViewModel by requestViewModels()
  private val sharedViewModel: SharedViewModel by activityViewModels()
  private val clickProxy = ClickProxy()
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var notifyInfoService: NotifyInfoService
  private var bound = false
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

//  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//    binding = FragmentMainBinding.inflate(inflater, container, false)
//    binding.bind(viewLifecycleOwner, viewModel)
//    return binding.root
//  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.bind(viewLifecycleOwner, viewModel)
    binding.apply {
      val navController = requireActivity().findNavController(R.id.fragment_main)
      appBarConfiguration = AppBarConfiguration(setOf(R.id.infoListFragment), drawerLayout)
      toolbar.setupWithNavController(navController, appBarConfiguration)
      toolbar.title = "搜索"
      toolbar.inflateMenu(R.menu.main)
      toolbar.setOnMenuItemClickListener(clickProxy::onOptionsItemSelected)
      navView.setupWithNavController(navController)
      navView.setNavigationItemSelectedListener(clickProxy::onNavItemSelected)

      val viewModel = this@MainFragment.viewModel
      val switchNotification: SwitchCompat = navView.menu.findItem(R.id.nav_notification).actionView.findViewById(R.id.switch_drawer)
      switchNotification.setOnCheckedChangeListener(clickProxy::onNotificationSwitchChecked)
      viewModel.isShowNotification.observe(viewLifecycleOwner) {
        switchNotification.isChecked = viewModel.isShowNotification.value!!
      }
      val switchNightMode: SwitchCompat = navView.menu.findItem(R.id.nav_night_mode).actionView.findViewById(R.id.switch_drawer)
      switchNightMode.setOnCheckedChangeListener(clickProxy::onNightModeSwitchChecked)
      viewModel.isNightMode.observe(viewLifecycleOwner) {
        switchNightMode.isChecked = viewModel.isNightMode.value!!
      }
    }

    viewModel.isShowNotification.observe(viewLifecycleOwner) { isChecked ->
      if (isChecked) {
        NotifyInfoService.startRepeatedly(requireActivity())
      } else {
        NotifyInfoService.stop(requireActivity())
      }
    }

    sharedViewModel.showNotificationEvent.observe(viewLifecycleOwner) {
      if (viewModel.isShowNotification.value!!) {
        NotifyInfoService.startRepeatedly(requireActivity())
      }
    }
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

  inner class ClickProxy {
    private var intervalMillis: Int by sharedPreferences(KEY_UPDATE_INTERVALS, 6)

    fun onNavItemSelected(menuItem: MenuItem) = run {
      when (menuItem.itemId) {
        R.id.nav_check_update -> {
          onCheckBtnClick()
          binding.drawerLayout.closeDrawers()
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
      materialDialog {
        title = "请选择刷新的间隔时间"
        setSingleChoiceItems(arrayOf("5 分钟", "10 分钟", "1 小时"), 0) { dialog, which ->
          intervalMillis = when (which) {
            0 -> 5
            1 -> 10
            2 -> 60
            else -> return@setSingleChoiceItems
          }
          viewModel.showNotification(false)
          viewModel.showNotification(true)
          binding.drawerLayout.closeDrawers()
          dialog.dismiss()
        }
      }.show()
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
      loginRequestViewModel.logout().observe(viewLifecycleOwner) {
        findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
      }
    }

    fun onNotificationSwitchChecked(isChecked: Boolean) {
      viewModel.showNotification(isChecked)
      if (!isChecked && bound) {
        notifyInfoService.hideNotification()
      }
    }

    fun onNightModeSwitchChecked(isChecked: Boolean) {
      viewModel.changeNightMode(isChecked)
    }
  }

}
