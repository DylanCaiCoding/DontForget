package com.dylanc.dontforget.ui.main.insert_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dylanc.dontforget.BR
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.loading.NavIconType
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.data.net.loadingDialog
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.utils.lifecycleOwner
import com.dylanc.dontforget.data.net.observe
import com.dylanc.dontforget.utils.setToolbar
import com.dylanc.dontforget.viewmodel.request.InfoRequestViewModel
import com.dylanc.utilktx.toast

class InsertInfoFragment : Fragment() {

  private val viewModel: InsertInfoViewModel by viewModels()
  private val requestViewModel: InfoRequestViewModel by viewModels()
  private val loadingDialog: LoadingDialog by loadingDialog()
  private val args: InsertInfoFragmentArgs by navArgs()
  private val clickProxy = ClickProxy()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_insert_info, container, false)
    bindView(view, viewModel, BR.clickProxy to clickProxy)
    return view.setToolbar(toolbarTitle, NavIconType.BACK).decorView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.title.value = args.info?.title
    requestViewModel.loading.observe(this, loadingDialog)
    requestViewModel.exception.observe(this)
  }

  private val toolbarTitle: String
    get() = if (args.info == null) "添加信息" else "修改信息"

  inner class ClickProxy {
//    fun onOptionsItemSelected(item: MenuItem) =
//      when (item.itemId) {
//        R.id.action_complete -> {
//          onCompleteBtnClick()
//          true
//        }
//        else -> false
//      }

    fun onCompleteBtnClick() {
      val value = viewModel.title.value
      val info = args.info
      val request = if (info == null) {
        requestViewModel.addInfo(value)
      } else {
        requestViewModel.updateInfo(info.id, value, info.dateStr)
      }
      request.observe(lifecycleOwner) {
        findNavController().popBackStack()
      }
    }

  }

}