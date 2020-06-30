package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.persistentCookieJar
import com.dylanc.dontforget.data.net.responseHandler
import com.dylanc.retrofit.helper.coroutines.request
import com.dylanc.dontforget.data.repository.api.UserApi
import com.dylanc.dontforget.data.repository.db.UserDao
import com.dylanc.dontforget.data.repository.db.appDatabase
import com.dylanc.retrofit.helper.apiServiceOf

/**
 * @author Dylan Cai
 */

val userRepository: UserRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
  UserRepository()
}

class UserRepository(
  private val model: UserModel = UserModel(),
  private val remoteDataSource: UserRemoteDataSource = UserRemoteDataSource()
) {

  suspend fun login(username: String, password: String) =
    remoteDataSource.requestLogin(username, password)
      .apply {
        model.updateUser(data!!)
      }

  suspend fun logout() =
    remoteDataSource.requestLogout()
      .apply {
        model.logout()
        persistentCookieJar.clear()
      }

  suspend fun isLogin() =
    model.isLogin()

  suspend fun register(username: String, password: String, confirmPassword: String) =
    remoteDataSource.requestRegister(username, password, confirmPassword)
}

class UserModel(private val userDao: UserDao = appDatabase.userDao()) {

  suspend fun updateUser(user: User) {
    userDao.deleteAll()
    userDao.insert(user)
  }

  suspend fun logout() {
    if (isLogin()) {
      userDao.deleteAll()
    }
  }

  suspend fun isLogin() =
    userDao.getUserList().isNotEmpty()
}

class UserRemoteDataSource {
  suspend fun requestLogin(username: String, password: String) = request(responseHandler) {
    apiServiceOf<UserApi>().login(username, password)
  }

  suspend fun requestLogout() = request(responseHandler) {
    apiServiceOf<UserApi>().logout()
  }

  suspend fun requestRegister(
    username: String,
    password: String,
    confirmPassword: String
  ) = request(responseHandler) {
    apiServiceOf<UserApi>().register(username, password, confirmPassword)
  }
}
