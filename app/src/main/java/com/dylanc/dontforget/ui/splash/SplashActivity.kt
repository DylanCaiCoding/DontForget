package com.dylanc.dontforget.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.repository.isLogin
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.utilktx.setStatusBarLightMode
import com.dylanc.utilktx.startActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    setStatusBarLightMode(true)

    GlobalScope.launch {
      delay(1000)
      if (isLogin()) {
        startActivity<MainActivity>()
      } else {
        startActivity<LoginActivity>()
      }
      finish()
    }

  }
}
