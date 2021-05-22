package com.dylanc.dontforget.ui.insert_info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dylanc.dontforget.adapter.loading.NavIconType
import com.dylanc.dontforget.base.BaseFragment
import com.dylanc.dontforget.databinding.FragmentInsertInfoBinding
import com.dylanc.dontforget.utils.bind
import com.dylanc.dontforget.utils.requestViewModels
import com.dylanc.dontforget.viewmodel.request.InfoRequestViewModel
import com.dylanc.viewbinding.BR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertInfoFragment : BaseFragment<FragmentInsertInfoBinding>() {

  private val viewModel: InsertInfoViewModel by viewModels()
  private val requestViewModel: InfoRequestViewModel by requestViewModels()
  private val args: InsertInfoFragmentArgs by navArgs()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.bind(viewModel,BR.clickProxy to ClickProxy())
    addToolbar(toolbarTitle, NavIconType.BACK)
    viewModel.title.value = args.info?.title
  }

  private val toolbarTitle: String
    get() = if (args.info == null) "添加信息" else "修改信息"

  inner class ClickProxy {

    fun onCompleteBtnClick() {
      val value = viewModel.title.value
      val info = args.info
      val request = if (info == null) {
        requestViewModel.addInfo(value)
      } else {
        requestViewModel.updateInfo(info.id, value, info.dateStr)
      }
      request.observe(viewLifecycleOwner) {
        findNavController().popBackStack()
      }
    }

  }

}
