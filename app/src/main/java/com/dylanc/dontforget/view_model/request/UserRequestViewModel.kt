package com.dylanc.dontforget.view_model.request

import androidx.lifecycle.liveData
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.repository.userRepository

class UserRequestViewModel : RequestViewModel() {

  fun login(username: String, password: String) = liveData(requestExceptionHandler) {
    emit(userRepository.login(username, password))
  }

  fun logout() = liveData(requestExceptionHandler) {
    emit(userRepository.logout())
  }

}