package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.api.UserApi
import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.bean.parseData
import com.dylanc.dontforget.data.db.InfoDao
import com.dylanc.dontforget.data.db.UserDao
import com.dylanc.retrofit.helper.cookie.clearPersistentCookies

/**
 * @author Dylan Cai
 */

class UserRepository(
  private val localDataSource: UserLocalDataSource,
  private val remoteDataSource: UserRemoteDataSource
) {

  suspend fun isLogin() = localDataSource.isLogin()

  suspend fun login(username: String, password: String): User {
    val user = remoteDataSource.requestLogin(username, password)
    localDataSource.updateUser(user)
    return user
  }

  suspend fun logout(): Any {
    val data = remoteDataSource.requestLogout()
    localDataSource.logout()
    clearPersistentCookies()
    return data
  }

  suspend fun register(username: String, password: String, confirmPassword: String): User {
    return remoteDataSource.requestRegister(username, password, confirmPassword)
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
