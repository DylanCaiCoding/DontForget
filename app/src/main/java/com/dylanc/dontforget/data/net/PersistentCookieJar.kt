package com.dylanc.dontforget.data.net

import android.content.Context
import com.dylanc.retrofit.helper.RetrofitHelper
import com.dylanc.retrofit.helper.okHttpClient
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author Dylan Cai
 * @since 2020/4/16
 */
private lateinit var persistentCookieJar: PersistentCookieJar

fun RetrofitHelper.Builder.persistentCookies(context: Context) = apply {
  persistentCookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
  cookieJar(persistentCookieJar)
}

fun clearCookieJar() {
  persistentCookieJar.clear()
}