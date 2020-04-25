package com.dylanc.dontforget.data.repository

import com.dylanc.dontforget.data.bean.User
import com.dylanc.dontforget.data.net.persistentCookieJar
import com.dylanc.utilktx.*

/**
 * @author Dylan Cai
 * @since 2019/10/30
 */
object UserRepository {
  private const val KEY_USER = "user"
  private var userCache: User? = null

  @JvmStatic
  fun getUser(): User? {
    if (userCache == null) {
      val json = spValueOf(KEY_USER, null)
      userCache = json?.toInstance()
    }
    return userCache
  }

  @JvmStatic
  fun saveUser(user: User) {
    userCache = user
    putSP(KEY_USER, user.toJson())
  }

  @JvmStatic
  fun logout() {
    if (isLogin()) {
      userCache = null
      removeSp(KEY_USER)
      persistentCookieJar.clear()
    }
  }

  @JvmStatic
  fun isLogin() = getUser() != null
}