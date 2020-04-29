package com.dylanc.dontforget.ui.main.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drakeet.multitype.MultiTypeAdapter
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.recycler.DontForgetInfoDelegate
import com.dylanc.dontforget.data.constant.REQUEST_CODE_ALARM_NOTIFY
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.dontforget.databinding.FragmentHomeBinding
import com.dylanc.dontforget.service.AlarmNotifyService
import com.dylanc.dontforget.view_model.request.InfoRequestViewModel
import java.util.*

class HomeFragment : Fragment() {

  private lateinit var binding:FragmentHomeBinding
  private val viewModel: HomeViewModel by viewModels()
  private val infoRequestViewModel: InfoRequestViewModel by viewModels()

  private val adapter = MultiTypeAdapter()
//  private val adapter = DontForgetInfoAdapter()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val view = inflater.inflate(R.layout.fragment_home, container, false)
    binding = DataBindingUtil.bind(view)!!
    adapter.register(DontForgetInfoDelegate())
    binding.adapter = adapter
    binding.viewModel = infoRequestViewModel
    binding.lifecycleOwner = this
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    infoRequestViewModel.requestList(requireActivity())
    infoRequestViewModel.list.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
      startNotifyAlarm()
    })
  }

  private fun startNotifyAlarm() {
    if (AlarmNotifyService.alreadyStarted || DontForgetInfoRepository.infos.isEmpty()) {
      return
    }

    val intent = Intent(activity, AlarmNotifyService::class.java)
    val pendingIntent = PendingIntent.getService(
      activity, REQUEST_CODE_ALARM_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerAtMillis = Date().time
    val intervalMillis = 60 * 2000
    alarmManager.setRepeating(
      AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis.toLong(), pendingIntent
    )
  }

}
