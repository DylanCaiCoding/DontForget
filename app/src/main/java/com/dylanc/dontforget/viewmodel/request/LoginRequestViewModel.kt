package com.dylanc.dontforget.viewmodel.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dylanc.dontforget.base.RequestViewModel
import com.dylanc.dontforget.data.repository.userRepository
import com.dylanc.retrofit.helper.coroutines.catch
import com.dylanc.retrofit.helper.coroutines.showLoading
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginRequestViewModel : RequestViewModel() {

  enum class AuthenticationState {
    UNAUTHENTICATED,
    AUTHENTICATED,
    INVALID_AUTHENTICATION
  }

  val authenticationState = MutableLiveData<AuthenticationState>()

  init {
    viewModelScope.launch{
      if (userRepository.isLogin()){
        authenticationState.value = AuthenticationState.AUTHENTICATED
      }else{
        authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
      }
    }
  }

  fun login(username: String?, password: String?) =
    userRepository.login(username, password)
      .showLoading(isLoading)
      .onEach {
        authenticationState.value = AuthenticationState.AUTHENTICATED
      }
      .catch(exception)
      .asLiveData()

  fun logout() =
    userRepository.logout()
      .showLoading(isLoading)
      .onEach {
        authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
      }
      .catch(exception)
      .asLiveData()

  fun register(username: String?, password: String?, confirmPassword: String?) =
    userRepository.register(username, password, confirmPassword)
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

}