package com.dylanc.dontforget.ui.main.home

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
import com.drakeet.multitype.MultiTypeAdapter
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.recycler.DateViewDelegate
import com.dylanc.dontforget.adapter.recycler.InfoGroupViewDelegate
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.DontForgetInfoGroup
import com.dylanc.dontforget.data.constant.*
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.dontforget.databinding.FragmentHomeBinding
import com.dylanc.dontforget.service.NotifyService
import com.dylanc.dontforget.ui.main.info.InfoActivity
import com.dylanc.dontforget.view_model.request.InfoRequestViewModel
import com.dylanc.utilktx.startActivityForResult
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment() {

  private lateinit var binding: FragmentHomeBinding
  private val viewModel: HomeViewModel by viewModels()
  private val infoRequestViewModel: InfoRequestViewModel by viewModels()

  private val adapter = MultiTypeAdapter()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val view = inflater.inflate(R.layout.fragment_home, container, false)
    adapter.register(DateViewDelegate())
    adapter.register(InfoGroupViewDelegate())
    binding = DataBindingUtil.bind(view)!!
    binding.adapter = adapter
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    infoRequestViewModel.requestList(requireActivity())
    infoRequestViewModel.list.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
      startNotifyAlarm()
      refresh_layout.isRefreshing = false

      val items = mutableListOf<Any>()
      var infoGroup: DontForgetInfoGroup? = null
      for (item in it) {
        if (item is DontForgetInfo) {
          when {
            infoGroup == null -> {
              infoGroup = DontForgetInfoGroup(item.dateStr, mutableListOf())
              infoGroup.list.add(item)
            }
            infoGroup.dateStr == item.dateStr -> {
              infoGroup.list.add(item)
            }
            else -> {
              items.add(infoGroup.dateStr)
              items.add(infoGroup)
              infoGroup = DontForgetInfoGroup(item.dateStr, mutableListOf())
              infoGroup.list.add(item)
            }
          }
        }
      }
      infoGroup?.let { it1 ->
        items.add(it1.dateStr)
        items.add(it1)
      }
      viewModel.list.value = items
    })
    refresh_layout.setOnRefreshListener {
      infoRequestViewModel.requestList(requireActivity())
    }
    fab.setOnClickListener {
      activity?.startActivityForResult<InfoActivity>(REQUEST_CODE_ADD_INFO) { resultCode, data ->
        if (resultCode == Activity.RESULT_OK && data != null) {
          val newInfo = data.getParcelableExtra(KEY_INFO) as DontForgetInfo
          val editMode = data.getBooleanExtra(KEY_EDIT_MODE, false)
          val list = infoRequestViewModel.list.value!!
          if (editMode) {
            if (list.size > 0) {
              for (i in list.indices) {
                val info = list[i] as DontForgetInfo
                if (newInfo.id == info.id) {
                  list[i] = newInfo
                  DontForgetInfoRepository.updateInfo(i, newInfo)
                  break
                }
              }
            }
          } else {
            DontForgetInfoRepository.addInfo(newInfo)
            if (list.size > 0) {
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
          }
          infoRequestViewModel.list.value = list
        }
      }
    }
    LiveEventBus
      .get(EVENT_NOTIFY, Boolean::class.java)
      .observe(this, androidx.lifecycle.Observer<Boolean> {
        if (it){
          startNotifyAlarm()
        }else{
          stopNotifyAlarm()
        }
      })
  }

  private fun startNotifyAlarm() {
    if (NotifyService.alreadyStarted || DontForgetInfoRepository.infos.isEmpty()) {
      return
    }

    val intent = Intent(activity, NotifyService::class.java)
    val pendingIntent = PendingIntent.getService(
      activity, REQUEST_CODE_ALARM_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerAtMillis = Date().time
    val intervalMillis = 60 * 1000 * 1
    alarmManager.setRepeating(
      AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis.toLong(), pendingIntent
    )
  }

  private fun stopNotifyAlarm(){
    val intent = Intent(activity, NotifyService::class.java)
    val pendingIntent = PendingIntent.getService(
      activity, REQUEST_CODE_ALARM_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
  }

}
