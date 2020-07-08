package com.dylanc.dontforget.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.utils.isDarkMode
import com.dylanc.utilktx.setStatusBarLightMode
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
    lifecycleScope.launch {
      delay(1000)
      if (UserRepository().isLogin()) {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
      } else {
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
      }
    }
  }
}