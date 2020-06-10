package com.dylanc.dontforget.ui.main.info_list

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
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.databinding.FragmentInfoListBinding
import com.dylanc.dontforget.service.NotifyInfoService
import com.dylanc.dontforget.ui.main.insert_info.InsertInfoActivity
import com.dylanc.dontforget.utils.alertItems
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.view_model.request.InfoRequestViewModel
import com.dylanc.dontforget.view_model.state.ListStateViewModel
import com.dylanc.liveeventbus.observeEvent
import kotlinx.android.synthetic.main.fragment_info_list.*

class InfoListFragment : Fragment() {

  //  private val viewModel: InfoListViewModel by viewModels()
  private val requestViewModel: InfoRequestViewModel by viewModels()
  private val listStateViewModel: ListStateViewModel by viewModels()
  private val clickProxy = ClickProxy()
  private val adapter = InfoAdapter(clickProxy::onItemClick, clickProxy::onItemLongClick)
  private val loadingDialog = LoadingDialog()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View =
    inflater.inflate(R.layout.fragment_info_list, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding: FragmentInfoListBinding = bindView(view)
    binding.adapter = adapter
    binding.viewModel = listStateViewModel
    binding.requestViewModel = requestViewModel
    binding.clickProxy = clickProxy
    binding.lifecycleOwner = this
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    refresh_layout.setColorSchemeResources(R.color.colorAccent)
    refresh_layout.setOnRefreshListener { requestViewModel.requestInfoList() }
    observeEvent(EVENT_NOTIFICATION, this::onNotificationEvent)
    listStateViewModel.isRefreshing.observe(viewLifecycleOwner, Observer { isRefreshing ->
      if (!isRefreshing) {
        activity?.let { NotifyInfoService.startRepeatedly(it) }
      }
    })
    requestViewModel.list.observe(viewLifecycleOwner, Observer {
      loadingDialog.dismiss()
      listStateViewModel.isRefreshing.value = false
    })
    requestViewModel.getInfoList()
  }

  private fun onNotificationEvent(isChecked: Boolean) {
    activity?.let { activity ->
      if (isChecked) {
        NotifyInfoService.startRepeatedly(activity)
      } else {
        NotifyInfoService.stop(activity)
      }
    }
  }

  inner class ClickProxy {
    fun onFabClick() {
      onItemClick(null)
    }

    fun onItemClick(item: DontForgetInfo?) {
      InsertInfoActivity.start(item)
    }

    fun onItemLongClick(item: DontForgetInfo) {
      activity?.let { activity ->
        activity.alertItems("关闭提醒", "删除") { text, _ ->
          when (text) {
            "关闭提醒" -> {
            }
            "删除" -> {
              loadingDialog.show(childFragmentManager)
              requestViewModel.deleteInfo(item)
            }
          }
        }
      }
    }
  }
}