package com.dylanc.dontforget.ui.main.insert_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.loading.NavIconType
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.utils.lifecycleOwner
import com.dylanc.dontforget.utils.observeException
import com.dylanc.dontforget.utils.setToolbar
import com.dylanc.dontforget.viewmodel.request.InfoRequestViewModel
import com.dylanc.utilktx.toast

class InsertInfoFragment : Fragment() {

  private val viewModel: InsertInfoViewModel by viewModels()
  private val requestViewModel: InfoRequestViewModel by viewModels()
  private val args: InsertInfoFragmentArgs by navArgs()
  private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_insert_info, container, false)
    bindView(view, viewModel)
    val loadingHelper = view.setToolbar(
      toolbarTitle, NavIconType.BACK, R.menu.text_complete, clickProxy::onOptionsItemSelected
    )
    return loadingHelper.decorView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.title.value = args.info?.title
    eventHandler.observe()
  }

  private val toolbarTitle: String
    get() = if (args.info == null) {
      "添加信息"
    } else {
      "修改信息"
    }

  inner class ClickProxy {
    fun onOptionsItemSelected(item: MenuItem) =
      when (item.itemId) {
        R.id.action_complete -> {
          onCompleteBtnClick()
          true
        }
        else -> false
      }

    private fun onCompleteBtnClick() {
      val value = viewModel.title.value
      if (value.isNullOrBlank()) {
        toast("请输入标题")
      } else {
        val info = args.info
        val request = if (info == null) {
          requestViewModel.addInfo(value)
        } else {
          requestViewModel.updateInfo(info.id, value, info.dateStr)
        }
        loadingDialog.show(true)
        request.observe(lifecycleOwner) {
          loadingDialog.show(false)
        }
      }
    }
  }

  inner class EventHandler {
    fun observe() {
      requestViewModel.exception
        .observeException(lifecycleOwner) {
          loadingDialog.show(false)
          toast(it.message)
        }
    }
  }

}
