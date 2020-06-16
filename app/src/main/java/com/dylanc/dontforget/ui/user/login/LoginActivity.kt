package com.dylanc.dontforget.ui.user.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.ui.user.register.RegisterActivity
import com.dylanc.dontforget.utils.bindContentView
import com.dylanc.dontforget.utils.lifecycleOwner
import com.dylanc.dontforget.utils.observeException
import com.dylanc.dontforget.view_model.request.UserRequestViewModel
import com.dylanc.utilktx.setStatusBarLightMode
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast

class LoginActivity : AppCompatActivity() {

  private val viewModel: LoginViewModel by viewModels()
  private val requestViewModel: UserRequestViewModel by viewModels()
  private val clickProxy = ClickProxy()
  private val eventHandler = EventHandler()
  private val loadingDialog = LoadingDialog()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bindContentView(
      R.layout.activity_login, viewModel,
      BR.clickProxy to clickProxy
    )
    setStatusBarLightMode(true)
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
      loadingDialog.show(supportFragmentManager)
      requestViewModel.login(username, password)
        .observe(lifecycleOwner, Observer {
          loadingDialog.dismiss()
          toast("登录成功")
          startActivity<MainActivity>()
          finish()
        })
    }

    fun onRegisterBtnClick() {
      startActivity<RegisterActivity>()
    }
  }

  inner class EventHandler {
    fun observe() {
      requestViewModel.requestException
        .observeException(lifecycleOwner) {
          loadingDialog.dismiss()
          toast(it.message)
        }
    }
  }
}
