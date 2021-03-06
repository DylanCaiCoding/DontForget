package com.dylanc.dontforget.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.constant.AuthenticationState
import com.dylanc.dontforget.viewmodel.request.LoginRequestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

  private val viewModel: LoginRequestViewModel by viewModels()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.fragment_splash, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycleScope.launch {
      delay(1000)
      viewModel.authenticationState.observe(viewLifecycleOwner) { state ->
        when (state) {
          AuthenticationState.AUTHENTICATED ->
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
          AuthenticationState.INVALID_AUTHENTICATION ->
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
          else -> {
          }
        }
      }
    }
  }
}