package com.dylanc.dontforget.ui.main.info_list

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
import androidx.lifecycle.Observer
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.recycler.InfoAdapter
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.EVENT_NOTIFICATION
import com.dylanc.dontforget.data.constant.KEY_SHOW_NOTIFICATION
import com.dylanc.dontforget.data.constant.KEY_UPDATE_INTERVALS
import com.dylanc.dontforget.data.constant.REQUEST_CODE_ALARM_NOTIFY
import com.dylanc.dontforget.databinding.FragmentInfoListBinding
import com.dylanc.dontforget.service.NotifyService
import com.dylanc.dontforget.ui.main.info.InsertInfoActivity
import com.dylanc.liveeventbus.observeEvent
import com.dylanc.utilktx.logDebug
import com.dylanc.utilktx.spValueOf
import com.dylanc.utilktx.toast
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
    viewModel.isRefreshing.observe(viewLifecycleOwner, Observer { isRefreshing ->
      if (!isRefreshing) {
        startNotifyAlarm()
      }
    })
    viewModel.list.observe(viewLifecycleOwner, Observer {
      toast("list 改变了")
    })
  }

  private fun onItemClick(item: DontForgetInfo? = null) {
    InsertInfoActivity.start(item)
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