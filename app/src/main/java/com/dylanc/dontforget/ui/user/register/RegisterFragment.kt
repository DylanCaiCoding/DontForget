package com.dylanc.dontforget.ui.user.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.loading.NavIconType
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.utils.*
import com.dylanc.dontforget.view_model.request.UserRequestViewModel
import com.dylanc.utilktx.toast

class RegisterFragment : Fragment() {

  private val viewModel: RegisterViewModel by viewModels()
  private val requestViewModel: UserRequestViewModel by viewModels()
  private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
  private val eventHandler = EventHandler()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_register, container, false)
    bindView(
      view, viewModel,
      BR.clickProxy to ClickProxy()
    )
    val loadingHelper = view.setToolbar("注册", NavIconType.BACK)
    return loadingHelper.decorView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    eventHandler.observe()
  }

  inner class ClickProxy {
    fun onRegisterBtnClick() {
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
      val confirmPassword = viewModel.confirmPassword.value
      if (confirmPassword.isNullOrBlank()) {
        toast("请再次输入密码")
        return
      }
      loadingDialog.show(true)
      requestViewModel.register(username, password, confirmPassword)
        .observe(lifecycleOwner, Observer {
          loadingDialog.show(false)
          toast("注册成功")
//          finish()
        })
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