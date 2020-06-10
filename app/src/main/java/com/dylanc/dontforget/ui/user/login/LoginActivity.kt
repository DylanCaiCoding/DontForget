package com.dylanc.dontforget.ui.user.login

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.databinding.ActivityLoginBinding
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.utils.bindContentView
import com.dylanc.dontforget.view_model.request.UserRequestViewModel
import com.dylanc.utilktx.setStatusBarLightMode
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast

class LoginActivity : AppCompatActivity() {

  private lateinit var binding:ActivityLoginBinding
  private val viewModel: LoginViewModel by viewModels()
  private val requestViewModel: UserRequestViewModel by viewModels()
  private val loadingDialog = LoadingDialog()
  private val clickProxy = ClickProxy()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = bindContentView(R.layout.activity_login)
    binding.viewModel = viewModel
    binding.clickProxy = clickProxy
    binding.lifecycleOwner = this
    setStatusBarLightMode(true)
    requestViewModel.user.observe(this, Observer { user ->
      if (user != null) {
        toast("登录成功")
        loadingDialog.dismiss()
        startActivity<MainActivity>()
        finish()
      }
    })
  }

  inner class ClickProxy{
    fun onLoginBtnClick() {
      val username = viewModel.username.value
      if (TextUtils.isEmpty(username)) {
        toast("请输入账号")
        return
      }
      val password = viewModel.password.value
      if (TextUtils.isEmpty(password)) {
        toast("请输入密码")
        return
      }
      loadingDialog.show(supportFragmentManager)
      requestViewModel.login(username!!, password!!)
    }
  }
}
