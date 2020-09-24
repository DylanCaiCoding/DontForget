package com.dylanc.dontforget.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.bean.parseData
import com.dylanc.dontforget.data.net.clearCookieJar
import com.dylanc.dontforget.data.api.UserApi
import com.dylanc.dontforget.data.constant.AuthenticationState
import com.dylanc.dontforget.data.db.InfoDao
import com.dylanc.dontforget.data.db.UserDao
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * @author Dylan Cai
 */

class UserRepository(
  private val localDataSource: UserLocalDataSource,
  private val remoteDataSource: UserRemoteDataSource
) {

  private val _authenticationState = MutableLiveData<AuthenticationState>()
  val authenticationState: LiveData<AuthenticationState> = _authenticationState

  init {
    runBlocking {
      if (localDataSource.isLogin()) {
        _authenticationState.value = AuthenticationState.AUTHENTICATED
      } else {
        _authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
      }
    }
  }

  fun login(username: String?, password: String?) = flow {
    checkNotNull(username) { "请输入账号" }
    checkNotNull(password) { "请输入密码" }
    val user = remoteDataSource.requestLogin(username, password)
    localDataSource.updateUser(user)
    _authenticationState.value = AuthenticationState.AUTHENTICATED
    emit(user)
  }

  fun logout() = flow {
    val data = remoteDataSource.requestLogout()
    localDataSource.logout()
    clearCookieJar()
    _authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
    emit(data)
  }

  fun register(username: String?, password: String?, confirmPassword: String?) = flow {
    checkNotNull(username) { "请输入账号" }
    checkNotNull(password) { "请输入密码" }
    checkNotNull(confirmPassword) { "请再次输入密码" }
    emit(remoteDataSource.requestRegister(username, password, confirmPassword))
  }
}

class UserLocalDataSource(private val userDao: UserDao, private val infoDao: InfoDao) {

  suspend fun updateUser(user: User) {
    userDao.deleteAll()
    userDao.insert(user)
  }

  suspend fun logout() {
    if (isLogin()) {
      userDao.deleteAll()
      infoDao.deleteAll()
    }
  }

  suspend fun isLogin() =
    userDao.getUserList().isNotEmpty()
}

class UserRemoteDataSource(private val api: UserApi) {
  suspend fun requestLogin(username: String, password: String) =
    api.login(username, password).parseData()

  suspend fun requestLogout() =
    api.logout().parseData()

  suspend fun requestRegister(username: String, password: String, confirmPassword: String) =
    api.register(username, password, confirmPassword).parseData()
}
