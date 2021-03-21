package com.dylanc.dontforget.viewmodel.request

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.constant.AuthenticationState
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.retrofit.helper.coroutines.livedata.catch
import com.dylanc.retrofit.helper.coroutines.livedata.showLoading
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LoginRequestViewModel @ViewModelInject constructor(
  private val userRepository: UserRepository
) : RequestViewModel() {

  private val _authenticationState = MutableLiveData<AuthenticationState>()
  val authenticationState: LiveData<AuthenticationState> get() = _authenticationState

  init {
    viewModelScope.launch {
      if (userRepository.isLogin()) {
        _authenticationState.value = AuthenticationState.AUTHENTICATED
      } else {
        _authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
      }
    }
  }

  fun login(username: String?, password: String?) =
    flow {
      checkNotNull(username) { "请输入账号" }
      checkNotNull(password) { "请输入密码" }
      emit(userRepository.login(username, password))
      _authenticationState.value = AuthenticationState.AUTHENTICATED
    }
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun logout() =
    flow {
      emit(userRepository.logout())
      _authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
    }
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun register(username: String?, password: String?, confirmPassword: String?) =
    flow {
      checkNotNull(username) { "请输入账号" }
      checkNotNull(password) { "请输入密码" }
      checkNotNull(confirmPassword) { "请再次输入密码" }
      emit(userRepository.register(username, password, confirmPassword))
    }
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

}