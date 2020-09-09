package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.clearCookieJar
import com.dylanc.dontforget.data.repository.api.UserApi
import com.dylanc.dontforget.data.repository.db.UserDao
import com.dylanc.dontforget.data.repository.db.appDatabase
import com.dylanc.retrofit.helper.apiOf
import kotlinx.coroutines.flow.flow

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

  fun login(username: String, password: String) = flow {
    emit(
      remoteDataSource.requestLogin(username, password)
        .apply {
          model.updateUser(data!!)
        })
  }

  fun logout() = flow {
    emit(
      remoteDataSource.requestLogout()
        .apply {
          model.logout()
          infoRepository.deleteAllInfo()
          clearCookieJar()
        })
  }

  suspend fun isLogin() = model.isLogin()

  fun register(username: String, password: String, confirmPassword: String) = flow {
    emit(
      remoteDataSource.requestRegister(username, password, confirmPassword)
    )
  }
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
  suspend fun requestLogin(username: String, password: String) =
    apiOf<UserApi>().login(username, password)

  suspend fun requestLogout() =
    apiOf<UserApi>().logout()


  suspend fun requestRegister(
    username: String,
    password: String,
    confirmPassword: String
  ) =
    apiOf<UserApi>().register(username, password, confirmPassword)

}
