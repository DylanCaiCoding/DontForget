package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.persistentCookieJar
import com.dylanc.utilktx.*

/**
 * @author Dylan Cai
 */
private const val KEY_USER = "user"
private var user: User? = null

val userCache: User?
  get() {
    if (user == null) {
      val json = spValueOf(KEY_USER, null)
      user = json?.toInstance()
    }
    return user
  }

fun saveUser(user: User) {
  com.dylanc.dontforget.data.repository.user = user
  putSP(KEY_USER, user.toJson())
}

fun logout() {
  if (isLogin()) {
    user = null
    removeSp(KEY_USER)
    persistentCookieJar.clear()
  }
}

fun isLogin() = userCache != null
