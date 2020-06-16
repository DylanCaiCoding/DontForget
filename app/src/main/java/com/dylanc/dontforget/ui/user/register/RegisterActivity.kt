package com.dylanc.dontforget.ui.user.register

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.TitleConfig
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.databinding.ActivityRegisterBinding
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.utils.bindContentView
import com.dylanc.dontforget.utils.lifecycleOwner
import com.dylanc.dontforget.utils.observeException
import com.dylanc.dontforget.utils.setToolbar
import com.dylanc.dontforget.view_model.request.UserRequestViewModel
import com.dylanc.utilktx.finishAllActivities
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast

class RegisterActivity : AppCompatActivity() {
  private val viewModel: RegisterViewModel by viewModels()
  private val requestViewModel: UserRequestViewModel by viewModels()
  private val loadingDialog = LoadingDialog()
  private val eventHandler = EventHandler()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bindContentView(
      R.layout.activity_register, viewModel,
      BR.clickProxy to ClickProxy()
    )
    setToolbar("注册", TitleConfig.Type.BACK)
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
      loadingDialog.show(supportFragmentManager)
      requestViewModel.register(username, password, confirmPassword)
        .observe(lifecycleOwner, Observer {
          loadingDialog.dismiss()
          toast("注册成功")
          finish()
        })
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