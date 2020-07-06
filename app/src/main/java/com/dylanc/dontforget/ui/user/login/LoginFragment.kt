package com.dylanc.dontforget.ui.user.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.ui.user.register.RegisterActivity
import com.dylanc.dontforget.utils.bindView
import com.dylanc.dontforget.utils.isDarkMode
import com.dylanc.dontforget.utils.lifecycleOwner
import com.dylanc.dontforget.utils.observeException
import com.dylanc.dontforget.view_model.request.UserRequestViewModel
import com.dylanc.utilktx.setStatusBarLightMode
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast

class LoginFragment : Fragment() {

  private val viewModel: LoginViewModel by viewModels()
  private val requestViewModel: UserRequestViewModel by viewModels()
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()
  private val loadingDialog = LoadingDialog()

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
    requireActivity().apply {
      setStatusBarLightMode(!isDarkMode())
    }
    loadingDialog.fragmentActivity = requireActivity()
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
          loadingDialog.show(false)
          toast("登录成功")
          startActivity<MainActivity>()
          requireActivity().finish()
        })
    }

    val onRegisterBtnClickListener = Navigation.createNavigateOnClickListener(R.id.action_register)

    fun onRegisterBtnClick() {
//      Navigation.createNavigateOnClickListener(R.id.action_register).onClick()
      startActivity<RegisterActivity>()
    }
  }

  inner class EventHandler {
    fun observe() {
      requestViewModel.requestException
        .observeException(lifecycleOwner) {
          loadingDialog.show(false)
          toast(it.message)
        }
    }
  }
}