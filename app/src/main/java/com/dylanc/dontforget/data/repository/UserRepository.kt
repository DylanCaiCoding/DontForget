package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.persistentCookieJar
import com.dylanc.dontforget.data.repository.db.userDatabase

/**
 * @author Dylan Cai
 */
private var userCache: User? = null
private val userDao = userDatabase.userDao()

val user: User?
  get() {
    if (userCache == null && userDao.getUser().isNotEmpty()) {
      userCache = userDao.getUser()[0]
    }
    return userCache
  }

suspend fun insert(user: User) {
  userCache = user
  userDao.deleteAll()
  userDao.insert(user)
}

suspend fun logout() {
  if (isLogin()) {
    userCache = null
    userDao.deleteAll()
    persistentCookieJar.clear()
  }
}

fun isLogin() = user != null
