package com.dylanc.dontforget.ui.main.info_list

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.recycler.InfoAdapter
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.*
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.dontforget.databinding.FragmentInfoListBinding
import com.dylanc.dontforget.service.NotifyService
import com.dylanc.dontforget.ui.main.info.InfoActivity
import com.dylanc.dontforget.view_model.request.InfoRequestViewModel
import com.dylanc.liveeventbus.observeEvent
import com.dylanc.utilktx.logDebug
import com.dylanc.utilktx.spValueOf
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class InfoListFragment : Fragment() {

  private lateinit var binding: FragmentInfoListBinding
  private val adapter = InfoAdapter()
  private val viewModel: InfoListViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View =
    inflater.inflate(R.layout.fragment_info_list, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = DataBindingUtil.bind(view)!!
    binding.adapter = adapter
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    refresh_layout.post { refresh_layout.isRefreshing = true }
    refresh_layout.setColorSchemeResources(R.color.colorAccent)
    fab.setOnClickListener { onAddBtnClick() }
    observeEvent(EVENT_NOTIFICATION, this::onNotificationEvent)
    viewModel.requestList(this)
    viewModel.list.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
      startNotifyAlarm()
      refresh_layout.isRefreshing = false
    })
  }

  private fun onAddBtnClick() {
    activity?.let { activity ->
      InfoActivity.start(activity) { resultCode, data ->
        if (resultCode == Activity.RESULT_OK && data != null) {
          val newInfo = data.getParcelableExtra(KEY_INFO) as DontForgetInfo
          val editMode = data.getBooleanExtra(KEY_EDIT_MODE, false)
          val list = viewModel.list.value!!.toMutableList()
          if (editMode) {
            if (list.isNotEmpty()) {
              for (i in list.indices) {
                val info = list[i]
                if (newInfo.id == info.id) {
                  list[i] = newInfo
                  DontForgetInfoRepository.updateInfo(i, newInfo)
                  break
                }
              }
            }
          } else {
            DontForgetInfoRepository.addInfo(newInfo)
            if (list.isNotEmpty()) {
              for (i in list.indices) {
                val info = list[i]
                if (newInfo.dateStr != info.dateStr || i == list.size - 1) {
                  list.add(i, newInfo)
                  break
                }
              }
            } else {
              list.add(newInfo)
            }
          }
          viewModel.list.value = list
        }
      }
    }
  }


  private fun onNotificationEvent(isChecked: Boolean) {
    if (isChecked) {
      startNotifyAlarm()
    } else {
      stopNotifyAlarm()
    }
  }

  private fun startNotifyAlarm() {
    if (NotifyService.alreadyStarted || DontForgetInfoRepository.infos.isEmpty() ||
      !spValueOf(KEY_SHOW_NOTIFICATION, true)
    ) {
      return
    }

    val intent = Intent(activity, NotifyService::class.java)
    val pendingIntent = PendingIntent.getService(
      activity, REQUEST_CODE_ALARM_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerAtMillis = Date().time
    val intervalMillis = 60 * 1000 * spValueOf(KEY_UPDATE_INTERVALS, 6)
    logDebug("intervals -> ${spValueOf(KEY_UPDATE_INTERVALS, 5)}")
    alarmManager.setRepeating(
      AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis.toLong(), pendingIntent
    )
  }

  private fun stopNotifyAlarm() {
    val intent = Intent(activity, NotifyService::class.java)
    val pendingIntent = PendingIntent.getService(
      activity, REQUEST_CODE_ALARM_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
  }
}