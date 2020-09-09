@file:Suppress("unused")

package com.dylanc.dontforget

import android.app.Application
import com.dylanc.dontforget.adapter.loading.LoadingAdapter
import com.dylanc.dontforget.data.net.clearCookieJar
import com.dylanc.dontforget.data.net.persistentCookies
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.loadinghelper.ViewType
import com.dylanc.retrofit.helper.initRetrofit
import com.dylanc.retrofit.helper.okHttpClient
import com.dylanc.retrofit.helper.retrofit
import com.dylanc.utilktx.logJson
import retrofit2.Retrofit
import update.UpdateAppUtils


/**
 * @author Dylan Cai
 * @since 2020/1/23
 */
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    initRetrofit {
      debug(BuildConfig.DEBUG)
      persistentCookies(applicationContext)
      addHttpLog { msg ->
        if (msg.startsWith("{") && msg.endsWith("}")) {
          logJson(msg)
        }
      }
    }

    LoadingHelper.setDefaultAdapterPool {
      register(ViewType.LOADING, LoadingAdapter())
    }

    UpdateAppUtils.init(this)
  }
}