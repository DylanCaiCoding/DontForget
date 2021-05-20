package com.dylanc.dontforget.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.loading.NavIconType
import com.dylanc.dontforget.utils.bind
import com.dylanc.dontforget.utils.requestViewModels
import com.dylanc.dontforget.utils.setToolbar
import com.dylanc.dontforget.viewmodel.request.LoginRequestViewModel
import com.dylanc.longan.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

  private val viewModel: RegisterViewModel by viewModels()
  private val requestViewModel: LoginRequestViewModel by requestViewModels()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.fragment_register, container, false)
      .bind(viewLifecycleOwner, viewModel, BR.clickProxy to ClickProxy())
      .setToolbar("注册", NavIconType.BACK).decorView

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