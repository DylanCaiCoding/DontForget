package com.dylanc.dontforget.view_model.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.data.repository.userRepository
import kotlinx.coroutines.launch

class UserRequestViewModel : ViewModel() {
  val user = userRepository.user

  fun login(username: String, password: String) = viewModelScope.launch {
    userRepository.login(username, password)
  }

  fun logout() = viewModelScope.launch {
    userRepository.logout()
  }

}