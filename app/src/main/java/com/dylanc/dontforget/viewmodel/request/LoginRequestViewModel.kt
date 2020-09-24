package com.dylanc.dontforget.viewmodel.request

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.constant.AuthenticationState
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.retrofit.helper.coroutines.catch
import com.dylanc.retrofit.helper.coroutines.showLoading
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginRequestViewModel @ViewModelInject constructor(
  private val userRepository: UserRepository
) : RequestViewModel() {

  fun login(username: String?, password: String?) =
    userRepository.login(username, password)
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun logout() =
    userRepository.logout()
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun register(username: String?, password: String?, confirmPassword: String?) =
    userRepository.register(username, password, confirmPassword)
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

}