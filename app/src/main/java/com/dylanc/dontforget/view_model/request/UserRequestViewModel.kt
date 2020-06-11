package com.dylanc.dontforget.view_model.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dylanc.dontforget.data.net.Resource
import com.dylanc.dontforget.data.repository.userRepository

class UserRequestViewModel : ViewModel() {

  fun login(username: String, password: String) = liveData {
    emit(Resource.loading())
    emit(userRepository.login(username, password))
  }

  fun logout() = liveData {
    emit(Resource.loading())
    emit(userRepository.logout())
  }

}