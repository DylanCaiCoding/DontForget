package com.dylanc.dontforget.ui.user.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.blankj.utilcode.util.BarUtils
import com.dylanc.dontforget.R
import com.dylanc.dontforget.databinding.ActivityLoginBinding
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.dontforget.utils.viewModelOf
import com.dylanc.utilktx.setStatusBarLightMode
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

  private val viewModel: LoginViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding: ActivityLoginBinding = setBindingContentView(R.layout.activity_login)
    setStatusBarLightMode(true)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this


    btn_login.setOnClickListener {
      viewModel.requestLogin(this)
    }
  }
}
