package com.dylanc.dontforget.ui.user.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.BarUtils
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.api.UserApi
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.databinding.ActivityLoginBinding
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.ui.main.MainViewModel
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

  private lateinit var viewModel: LoginViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding: ActivityLoginBinding =
      DataBindingUtil.setContentView(this, R.layout.activity_login)
    viewModel = LoginViewModel()
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    BarUtils.setStatusBarLightMode(this, true)

    btn_login.setOnClickListener {
      viewModel.requestLogin(this)
    }
  }
}
