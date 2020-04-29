package com.dylanc.dontforget.ui.user.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.BarUtils
import com.dylanc.dontforget.R
import com.dylanc.dontforget.databinding.ActivityLoginBinding
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.dontforget.utils.viewModelOf
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

  private lateinit var viewModel: LoginViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding: ActivityLoginBinding = setBindingContentView(R.layout.activity_login)
    viewModel = viewModelOf()
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    BarUtils.setStatusBarLightMode(this, true)

    btn_login.setOnClickListener {
      viewModel.requestLogin(this)
    }
  }
}
