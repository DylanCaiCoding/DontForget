package com.dylanc.dontforget.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.ui.main.MainActivity
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.dontforget.utils.isDarkMode
import com.dylanc.utilktx.setStatusBarLightMode
import com.dylanc.utilktx.startActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.fragment_splash, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requireActivity().apply {
      setStatusBarLightMode(!isDarkMode())
    }

    lifecycleScope.launch {
      delay(1000)
      if (UserRepository().isLogin()) {
        startActivity<MainActivity>()
      } else {
        Navigation.createNavigateOnClickListener(R.id.action_login).onClick(view)
      }
    }
  }
}