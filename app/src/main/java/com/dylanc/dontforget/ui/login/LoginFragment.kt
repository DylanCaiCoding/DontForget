package com.dylanc.dontforget.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.utils.requestViewModels
import com.dylanc.dontforget.viewmodel.request.LoginRequestViewModel
import com.dylanc.utilktx.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

  private val viewModel: LoginViewModel by viewModels()
  private val requestViewModel: LoginRequestViewModel by requestViewModels()
  private val clickProxy = ClickProxy()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.fragment_login, container, false)
      .apply { bindView(this, viewModel, BR.clickProxy to clickProxy) }

  inner class ClickProxy {
    fun onLoginBtnClick() {
      val username = viewModel.username.value
      val password = viewModel.password.value
      requestViewModel.login(username, password)
        .observe(viewLifecycleOwner) {
          toast("登录成功")
          findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }

    fun onRegisterBtnClick() {
      findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }
  }

}