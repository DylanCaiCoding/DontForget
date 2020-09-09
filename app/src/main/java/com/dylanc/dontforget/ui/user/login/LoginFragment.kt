package com.dylanc.dontforget.ui.user.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.utils.lifecycleOwner
import com.dylanc.dontforget.utils.observeException
import com.dylanc.dontforget.viewmodel.request.UserRequestViewModel
import com.dylanc.utilktx.toast

class LoginFragment : Fragment() {

  private val viewModel: LoginViewModel by viewModels()
  private val requestViewModel: UserRequestViewModel by viewModels()
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()
  private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_login, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindView(
      view, viewModel,
      BR.clickProxy to clickProxy
    )
    eventHandler.observe()
  }

  inner class ClickProxy {
    fun onLoginBtnClick() {
      val username = viewModel.username.value
      if (username.isNullOrBlank()) {
        toast("请输入账号")
        return
      }
      val password = viewModel.password.value
      if (password.isNullOrBlank()) {
        toast("请输入密码")
        return
      }
      loadingDialog.show(true)
      requestViewModel.login(username, password)
        .observe(lifecycleOwner, Observer {
          toast("登录成功")
          loadingDialog.show(false)
          findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        })
    }

    fun onRegisterBtnClick() {
      findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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