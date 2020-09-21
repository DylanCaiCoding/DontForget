package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.bean.parseData
import com.dylanc.dontforget.data.net.clearCookieJar
import com.dylanc.dontforget.data.repository.api.UserApi
import com.dylanc.dontforget.data.repository.db.UserDao
import com.dylanc.dontforget.data.repository.db.appDatabase
import com.dylanc.retrofit.helper.apiOf
import kotlinx.coroutines.flow.flow

/**
 * @author Dylan Cai
 */

val userRepository: UserRepository by lazy { UserRepository() }

class UserRepository(
  private val localDataSource: UserLocalDataSource = UserLocalDataSource(),
  private val remoteDataSource: UserRemoteDataSource = UserRemoteDataSource()
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
    infoRepository.deleteAllInfo()
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

class UserLocalDataSource(private val userDao: UserDao = appDatabase.userDao()) {

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
    apiOf<UserApi>().login(username, password).parseData()

  suspend fun requestLogout() = apiOf<UserApi>().logout().parseData()

  suspend fun requestRegister(username: String, password: String, confirmPassword: String) =
    apiOf<UserApi>().register(username, password, confirmPassword).parseData()

}
