package com.dylanc.dontforget.ui.register

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dylanc.dontforget.adapter.loading.NavIconType
import com.dylanc.dontforget.base.BaseFragment
import com.dylanc.dontforget.databinding.FragmentRegisterBinding
import com.dylanc.dontforget.utils.bind
import com.dylanc.dontforget.utils.requestViewModels
import com.dylanc.dontforget.viewmodel.request.LoginRequestViewModel
import com.dylanc.longan.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

  private val viewModel: RegisterViewModel by viewModels()
  private val requestViewModel: LoginRequestViewModel by requestViewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.bind(viewModel, BR.clickProxy to ClickProxy())
    addToolbar("注册", NavIconType.BACK)
  }

  inner class ClickProxy {
    fun onRegisterBtnClick() {
      val username = viewModel.username.value
      val password = viewModel.password.value
      val confirmPassword = viewModel.confirmPassword.value
      requestViewModel.register(username, password, confirmPassword).observe(viewLifecycleOwner) {
        toast("注册成功")
        findNavController().popBackStack()
      }
    }
  }
}