package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.bean.parseData
import com.dylanc.dontforget.data.net.clearCookieJar
import com.dylanc.dontforget.data.api.UserApi
import com.dylanc.dontforget.data.db.InfoDao
import com.dylanc.dontforget.data.db.UserDao
import kotlinx.coroutines.flow.flow

/**
 * @author Dylan Cai
 */

class UserRepository(
  private val localDataSource: UserLocalDataSource,
  private val remoteDataSource: UserRemoteDataSource
) {

  fun login(username: String?, password: String?) = flow {
    checkNotNull(username) { "请输入账号" }
    checkNotNull(password) { "请输入密码" }
    val user = remoteDataSource.requestLogin(username, password)
    localDataSource.updateUser(user)
    emit(user)
  }

  fun logout() = flow {
    val data = remoteDataSource.requestLogout()
    localDataSource.logout()
    clearCookieJar()
    emit(data)
  }

  suspend fun isLogin() = localDataSource.isLogin()

  fun register(username: String?, password: String?, confirmPassword: String?) = flow {
    checkNotNull(username) { "请输入账号" }
    checkNotNull(password) { "请输入密码" }
    checkNotNull(confirmPassword) { "请再次输入密码" }
    emit(remoteDataSource.requestRegister(username, password, confirmPassword))
  }
}

class UserLocalDataSource(private val userDao: UserDao,private val infoDao: InfoDao) {

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
