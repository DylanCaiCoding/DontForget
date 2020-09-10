package com.dylanc.dontforget.ui.main.info_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.recycler.InfoAdapter
import com.dylanc.dontforget.base.event.observeEvent
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_INFO
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.data.net.loadingDialog
import com.dylanc.dontforget.data.net.observe
import com.dylanc.dontforget.service.NotifyInfoService
import com.dylanc.dontforget.utils.alertItems
import com.dylanc.dontforget.utils.applicationViewModels
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.viewmodel.event.SharedViewModel
import com.dylanc.dontforget.viewmodel.request.InfoRequestViewModel
import com.dylanc.utilktx.bundleOf
import kotlinx.android.synthetic.main.fragment_info_list.*

class InfoListFragment : Fragment() {

  private val viewModel: InfoListViewModel by viewModels()
  private val requestViewModel: InfoRequestViewModel by viewModels()
  private val sharedViewModel: SharedViewModel by applicationViewModels()
  private val loadingDialog: LoadingDialog by loadingDialog()
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()
  private val adapter = InfoAdapter(clickProxy::onItemClick, clickProxy::onItemLongClick)

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View =
    inflater.inflate(R.layout.fragment_info_list, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindView(
      view, viewModel,
      BR.adapter to adapter,
      BR.clickProxy to clickProxy,
      BR.eventHandler to eventHandler,
      BR.requestViewModel to requestViewModel
    )
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    refresh_layout.setColorSchemeResources(R.color.colorAccent)
    requestViewModel.getInfoList()
      .observe(viewLifecycleOwner, Observer {
        NotifyInfoService.startRepeatedly(activity)
      })
    sharedViewModel.isShowNotification
      .observeEvent(viewLifecycleOwner) { isChecked ->
        if (isChecked) {
          NotifyInfoService.startRepeatedly(activity)
        } else {
          NotifyInfoService.stop(activity)
        }
      }
    requestViewModel.loading.observe(this, loadingDialog)
    requestViewModel.exception.observe(this)
  }

  inner class ClickProxy {
    fun onAddBtnClick() {
      requireActivity().findNavController(R.id.nav_host_fragment)
        .navigate(R.id.action_mainFragment_to_insertInfoFragment)
    }

    fun onItemClick(item: DontForgetInfo) {
      requireActivity().findNavController(R.id.nav_host_fragment)
        .navigate(R.id.action_mainFragment_to_insertInfoFragment, bundleOf(KEY_INFO to item))
    }

    fun onItemLongClick(item: DontForgetInfo) {
      activity?.alertItems("关闭提醒", "删除") { text, _ ->
        when (text) {
          "关闭提醒" -> {
          }
          "删除" -> {
            requestViewModel.deleteInfo(item)
              .observe(viewLifecycleOwner, Observer {

              })
          }
        }
      }
    }
  }

  inner class EventHandler {

    val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
      requestViewModel.requestInfoList()
        .observe(viewLifecycleOwner, Observer {
          NotifyInfoService.startRepeatedly(activity)
        })
    }

//    fun onRefresh() {
//      requestViewModel.requestInfoList()
//        .observe(viewLifecycleOwner, Observer {
//          NotifyInfoService.startRepeatedly(activity)
//        })
//    }
  }
}