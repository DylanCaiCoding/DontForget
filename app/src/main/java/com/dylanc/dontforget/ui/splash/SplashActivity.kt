package com.dylanc.dontforget.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.BarUtils
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.utilktx.startActivity
import kotlinx.coroutines.*
import update.UpdateAppUtils

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    BarUtils.setStatusBarLightMode(this, true)

    GlobalScope.launch {
      delay(1000)
      if (UserRepository.isLogin()) {
        startActivity<MainActivity>()
      } else {
        startActivity<LoginActivity>()
      }
      finish()
    }

  }
}
