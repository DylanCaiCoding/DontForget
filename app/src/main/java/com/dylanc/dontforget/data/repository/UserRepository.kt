package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.persistentCookieJar
import com.dylanc.dontforget.data.net.resource
import com.dylanc.dontforget.data.repository.api.UserApi
import com.dylanc.dontforget.data.repository.db.UserDao
import com.dylanc.dontforget.data.repository.db.userDatabase
import com.dylanc.retrofit.helper.apiServiceOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    resource {
      remoteDataSource.requestLogin(username, password)
        .apply {
          model.updateUser(data)
        }
    }

  suspend fun logout() =
    resource {
      remoteDataSource.requestLogout()
        .apply {
          model.logout()
          persistentCookieJar.clear()
        }
    }

  suspend fun isLogin() =
    model.isLogin()
}

class UserModel(private val userDao: UserDao = userDatabase.userDao()) {

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
    apiServiceOf<UserApi>().login(username, password)

  suspend fun requestLogout() =
    apiServiceOf<UserApi>().logout()

}
