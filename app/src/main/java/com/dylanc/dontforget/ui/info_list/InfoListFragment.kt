package com.dylanc.dontforget.ui.info_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_INFO
import com.dylanc.dontforget.ui.info_list.adapter.InfoAdapter
import com.dylanc.dontforget.utils.*
import com.dylanc.dontforget.viewmodel.request.InfoRequestViewModel
import com.dylanc.dontforget.viewmodel.shared.SharedViewModel
import com.dylanc.utilktx.bundleOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoListFragment : Fragment() {

  private val viewModel: InfoListViewModel by viewModels()
  private val requestViewModel: InfoRequestViewModel by requestViewModels()
  private val sharedViewModel: SharedViewModel by activityViewModels()
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()
  private val adapter = InfoAdapter(clickProxy::onItemClick, clickProxy::onItemLongClick)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.fragment_info_list, container, false)
      .bind(
        viewLifecycleOwner, viewModel,
        BR.adapter to adapter,
        BR.requestViewModel to requestViewModel,
        BR.clickProxy to clickProxy,
        BR.eventHandler to eventHandler
      )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requestViewModel.initInfoList()
      .observe(viewLifecycleOwner) {
        sharedViewModel.showNotificationEvent.post()
      }
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
      materialDialog {
        items("关闭提醒", "删除") { text, _ ->
          when (text) {
            "关闭提醒" -> {
            }
            "删除" -> {
              requestViewModel.deleteInfo(item).observe(viewLifecycleOwner) {
              }
            }
          }
        }
      }.show()
    }
  }

  inner class EventHandler {

    val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
      requestViewModel.refreshInfoList().observe(viewLifecycleOwner) {
        sharedViewModel.showNotificationEvent.post()
      }
    }
  }

}