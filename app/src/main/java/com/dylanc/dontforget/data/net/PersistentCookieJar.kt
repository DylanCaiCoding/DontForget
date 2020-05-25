package com.dylanc.dontforget.data.net

import android.content.Context
import com.dylanc.retrofit.helper.Default
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor

/**
 * @author Dylan Cai
 * @since 2020/4/16
 */
fun Default.persistentCookies(context: Context): Default {
  persistentCookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
  return cookieJar(persistentCookieJar)
}

lateinit var persistentCookieJar: PersistentCookieJar