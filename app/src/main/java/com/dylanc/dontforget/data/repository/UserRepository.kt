package com.dylanc.dontforget.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dylanc.dontforget.data.repository.api.UserApi
import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.persistentCookieJar
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
  val user = model.user

  suspend fun login(username: String, password: String) {
    val user = remoteDataSource.requestLogin(username, password)
    model.updateUser(user)
  }

  suspend fun logout() {
    remoteDataSource.requestLogout()
    model.logout()
    persistentCookieJar.clear()
  }

  suspend fun isLogin() =
    model.isLogin()
}

class UserModel(private val userDao: UserDao = userDatabase.userDao()) {

  private val _user: MutableLiveData<User> = MutableLiveData()
  val user: LiveData<User> = _user

  suspend fun updateUser(user: User) {
    withContext(Dispatchers.IO) {
      userDao.deleteAll()
      userDao.insert(user)
    }
    withContext(Dispatchers.Main) {
      _user.value = user
    }
  }

  suspend fun logout() {
    withContext(Dispatchers.IO) {
      if (isLogin()) {
        userDao.deleteAll()
      }
    }
    withContext(Dispatchers.Main) {
      _user.value = null
    }
  }

  suspend fun isLogin() = withContext(Dispatchers.IO) {
    userDao.getUserList().isNotEmpty()
  }
}

class UserRemoteDataSource {
  suspend fun requestLogin(username: String, password: String) = withContext(Dispatchers.IO) {
    apiServiceOf<UserApi>().login(username, password).data
  }

  suspend fun requestLogout() = withContext(Dispatchers.IO) {
    apiServiceOf<UserApi>().logout().data
  }
}
