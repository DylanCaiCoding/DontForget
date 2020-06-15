package com.dylanc.dontforget.ui.main.info_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.recycler.InfoAdapter
import com.dylanc.dontforget.base.event.EventObserver
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.data.net.RequestException
import com.dylanc.dontforget.data.net.observeProcessed
import com.dylanc.dontforget.databinding.FragmentInfoListBinding
import com.dylanc.dontforget.service.NotifyInfoService
import com.dylanc.dontforget.ui.main.insert_info.InsertInfoActivity
import com.dylanc.dontforget.utils.alertItems
import com.dylanc.dontforget.utils.applicationViewModels
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.view_model.event.SharedViewModel
import com.dylanc.dontforget.view_model.request.InfoRequestViewModel
import com.dylanc.dontforget.view_model.state.ListStateViewModel
import com.dylanc.utilktx.toast
import kotlinx.android.synthetic.main.fragment_info_list.*

class InfoListFragment : Fragment() {

  //  private val viewModel: InfoListViewModel by viewModels()
  private lateinit var binding: FragmentInfoListBinding
  private val requestViewModel: InfoRequestViewModel by viewModels()
  private val listStateViewModel: ListStateViewModel by viewModels()
  private val sharedViewModel: SharedViewModel by applicationViewModels()
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
    refresh_layout.setColorSchemeResources(R.color.colorAccent)
    listStateViewModel.isRefreshing.value = true
    eventHandler.observe()
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

    fun observe(){
      requestViewModel.getInfoList()
        .observe(viewLifecycleOwner, Observer {
          NotifyInfoService.startRepeatedly(activity)
          listStateViewModel.isRefreshing.value = false
        })
      requestViewModel.requestException
        .observeProcessed(viewLifecycleOwner){
          loadingDialog.dismiss()
          listStateViewModel.isRefreshing.value = false
          toast(it.message)
        }
      sharedViewModel.showNotification
        .observe(viewLifecycleOwner, EventObserver { isChecked ->
          if (isChecked) {
            NotifyInfoService.startRepeatedly(activity)
          } else {
            NotifyInfoService.stop(activity)
          }
        })
    }

    val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
      requestViewModel.requestInfoList()
        .observe(viewLifecycleOwner, Observer {
          NotifyInfoService.startRepeatedly(activity)
          listStateViewModel.isRefreshing.value = false
        })
    }

    fun onRefresh() {
//      requestViewModel.requestInfoList()
//        .observe(viewLifecycleOwner, Observer {
//          NotifyInfoService.startRepeatedly(activity)
//          listStateViewModel.isRefreshing.value = false
//        })
    }
  }
}