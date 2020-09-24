package com.dylanc.dontforget.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.repository.UserRepository

class SplashViewModel @ViewModelInject constructor(
  userRepository: UserRepository
) : ViewModel() {
  val authenticationState = userRepository.authenticationState
}