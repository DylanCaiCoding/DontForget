package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.api.UserApi
import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.persistentCookieJar
import com.dylanc.dontforget.data.repository.db.UserDao
import com.dylanc.dontforget.data.repository.db.userDatabase
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.rxjava.io2mainThread

/**
 * @author Dylan Cai
 */
class UserRepository(
  private val model: UserModel = UserModel(),
  private val remoteDataSource: UserRemoteDataSource = UserRemoteDataSource()
) {
  fun requestLogin(username: String, password: String) =
    remoteDataSource.requestLogin(username, password)

  suspend fun updateUser(user: User) =
    model.updateUser(user)

  suspend fun logout() =
    model.logout()

  suspend fun isLogin() =
    model.isLogin()
}

class UserModel(private val userDao: UserDao = userDatabase.userDao()) {

  suspend fun getUser(): User? =
    if (isLogin()) {
      userDao.getUserList()[0]
    } else {
      null
    }

  suspend fun updateUser(user: User) {
    userDao.deleteAll()
    userDao.insert(user)
  }

  suspend fun logout() {
    if (isLogin()) {
      userDao.deleteAll()
      persistentCookieJar.clear()
    }
  }

  suspend fun isLogin() =
    userDao.getUserList().isNotEmpty()
}

class UserRemoteDataSource {
  fun requestLogin(username: String, password: String) =
    apiServiceOf<UserApi>()
      .login(username, password)
      .io2mainThread()
}