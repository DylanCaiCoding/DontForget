package com.dylanc.dontforget.ui.main.info_list

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.recycler.InfoAdapter
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.*
import com.dylanc.dontforget.databinding.FragmentInfoListBinding
import com.dylanc.dontforget.service.NotifyService
import com.dylanc.dontforget.ui.main.info.InsertInfoActivity
import com.dylanc.liveeventbus.observeEvent
import com.dylanc.utilktx.logDebug
import com.dylanc.utilktx.spValueOf
import kotlinx.android.synthetic.main.fragment_info_list.*
import java.util.*

class InfoListFragment : Fragment() {

  private val viewModel: InfoListViewModel by viewModels()
  private val adapter = InfoAdapter(this::onItemClick)

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View =
    inflater.inflate(R.layout.fragment_info_list, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = DataBindingUtil.bind<FragmentInfoListBinding>(view)!!
    binding.adapter = adapter
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    refresh_layout.setColorSchemeResources(R.color.colorAccent)
    refresh_layout.setOnRefreshListener { viewModel.requestInfoList() }
    fab.setOnClickListener { onItemClick() }
    observeEvent(EVENT_NOTIFICATION, this::onNotificationEvent)
    viewModel.getInfoList()
    viewModel.isRefreshing.observe(viewLifecycleOwner, androidx.lifecycle.Observer { isRefreshing ->
      if (!isRefreshing) {
        startNotifyAlarm()
      }
    })
  }

  private fun onItemClick(item: DontForgetInfo? = null) {
    activity?.let { activity ->
      InsertInfoActivity.start(activity, item) { resultCode, data ->
        if (resultCode == Activity.RESULT_OK && data != null) {
          val info = data.getParcelableExtra(KEY_INFO) as DontForgetInfo
          viewModel.insertInfo(info)
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
    if (NotifyService.alreadyStarted || !spValueOf(KEY_SHOW_NOTIFICATION, true)) {
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