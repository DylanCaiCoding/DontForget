@file:Suppress("unused")

package com.dylanc.dontforget

import android.app.Application
import com.dylanc.dontforget.adapter.loading.LoadingAdapter
import com.dylanc.longan.isJson
import com.dylanc.longan.logJson
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.loadinghelper.ViewType
import com.dylanc.retrofit.helper.cookie.persistentCookies
import com.dylanc.retrofit.helper.initRetrofit
import dagger.hilt.android.HiltAndroidApp

/**
 * @author Dylan Cai
 * @since 2020/1/23
 */
@HiltAndroidApp
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    initRetrofit {
      debug(BuildConfig.DEBUG)
      persistentCookies(applicationContext)
      addHttpLog { msg ->
        if (msg.isJson()) {
          logJson(msg)
        }
      }
    }

    LoadingHelper.setDefaultAdapterPool {
      register(ViewType.LOADING, LoadingAdapter())
    }

  }
}