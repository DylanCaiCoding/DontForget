package com.dylanc.dontforget.ui.main.info_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.recycler.InfoAdapter
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.EVENT_NOTIFICATION
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.data.net.RequestException
import com.dylanc.dontforget.data.net.observeProcessed
import com.dylanc.dontforget.databinding.FragmentInfoListBinding
import com.dylanc.dontforget.service.NotifyInfoService
import com.dylanc.dontforget.ui.main.insert_info.InsertInfoActivity
import com.dylanc.dontforget.utils.alertItems
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.view_model.request.InfoRequestViewModel
import com.dylanc.dontforget.view_model.state.ListStateViewModel
import com.dylanc.liveeventbus.observeEvent
import com.dylanc.utilktx.toast
import kotlinx.android.synthetic.main.fragment_info_list.*

class InfoListFragment : Fragment() {

  //  private val viewModel: InfoListViewModel by viewModels()
  private lateinit var binding: FragmentInfoListBinding
  private val requestViewModel: InfoRequestViewModel by viewModels()
  private val listStateViewModel: ListStateViewModel by viewModels()
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()
  private val adapter = InfoAdapter(clickProxy::onItemClick, clickProxy::onItemLongClick)
  private val loadingDialog = LoadingDialog()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View =
    inflater.inflate(R.layout.fragment_info_list, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = bindView(view)
    binding.adapter = adapter
    binding.viewModel = listStateViewModel
    binding.requestViewModel = requestViewModel
    binding.clickProxy = clickProxy
    binding.eventHandler = eventHandler
    binding.lifecycleOwner = this
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    eventHandler.observe()
    refresh_layout.setColorSchemeResources(R.color.colorAccent)
    refresh_layout.setOnRefreshListener(eventHandler::onRefresh)
        listStateViewModel.isRefreshing.value = true
    requestViewModel.getInfoList()
      .observe(viewLifecycleOwner, Observer {
        NotifyInfoService.startRepeatedly(activity)
        listStateViewModel.isRefreshing.value = false
      })
  }

  inner class ClickProxy {

    fun onAddBtnClick() {
      InsertInfoActivity.start()
    }

    fun onItemClick(item: DontForgetInfo) {
      InsertInfoActivity.start(item)
    }

    fun onItemLongClick(item: DontForgetInfo) {
      activity?.alertItems("关闭提醒", "删除") { text, _ ->
        when (text) {
          "关闭提醒" -> {
          }
          "删除" -> {
            loadingDialog.show(childFragmentManager)
            requestViewModel.deleteInfo(item)
              .observe(viewLifecycleOwner, Observer {
                loadingDialog.dismiss()
              })
          }
        }
      }
    }
  }

  inner class EventHandler {

    fun observe() {
      observeEvent(EVENT_NOTIFICATION, this::onNotificationEvent)
      requestViewModel.requestException
        .observeProcessed(viewLifecycleOwner, this::onRequestException)
    }

    private fun onRequestException(e: RequestException) {
      loadingDialog.dismiss()
      listStateViewModel.isRefreshing.value = false
      toast(e.message)
    }

    fun onRefresh() {
      requestViewModel.requestInfoList()
        .observe(viewLifecycleOwner, Observer {
          NotifyInfoService.startRepeatedly(activity)
          listStateViewModel.isRefreshing.value = false
        })
    }

    private fun onNotificationEvent(isChecked: Boolean) {
      if (isChecked) {
        NotifyInfoService.startRepeatedly(activity)
      } else {
        NotifyInfoService.stop(activity)
      }
    }
  }
}