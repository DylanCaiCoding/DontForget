package com.dylanc.dontforget.viewmodel.request

import androidx.lifecycle.asLiveData
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.repository.userRepository
import com.dylanc.retrofit.helper.coroutines.catch
import com.dylanc.retrofit.helper.coroutines.showLoading

class UserRequestViewModel : RequestViewModel() {

  fun login(username: String, password: String) =
    userRepository.login(username, password)
      .showLoading(loading)
      .catch(exception)
      .asLiveData()

  fun logout() =
    userRepository.logout()
      .showLoading(loading)
      .catch(exception)
      .asLiveData()

  fun register(username: String, password: String, confirmPassword: String) =
    userRepository.register(username, password, confirmPassword)
      .showLoading(loading)
      .catch(exception)
      .asLiveData()

}