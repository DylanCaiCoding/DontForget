package com.dylanc.dontforget.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.dontforget.utils.isDarkMode
import com.dylanc.utilktx.setStatusBarLightMode
import com.dylanc.utilktx.startActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_splash)
    setStatusBarLightMode(!isDarkMode())

    lifecycleScope.launch {
      delay(1000)
      if (UserRepository().isLogin()) {
        startActivity<MainActivity>()
      } else {
        startActivity<LoginActivity>()
      }
      finish()
    }
  }

}
