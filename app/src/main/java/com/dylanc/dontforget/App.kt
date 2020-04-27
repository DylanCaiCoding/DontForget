@file:Suppress("unused")

package com.dylanc.dontforget

import android.app.Application
import com.dylanc.dontforget.adapter.loading.LoadingAdapter
import com.dylanc.dontforget.data.constant.BASE_URL
import com.dylanc.dontforget.data.net.persistentCookies
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.loadinghelper.ViewType
import com.dylanc.retrofit.helper.RetrofitHelper
import com.dylanc.utilktx.logJson


/**
 * @author Dylan Cai
 * @since 2020/1/23
 */
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    RetrofitHelper.getDefault()
      .baseUrl(BASE_URL)
      .setDebug(true)
      .persistentCookies(this)
      .addHttpLoggingInterceptor { msg ->
        if (msg.startsWith("{") && msg.endsWith("}")) {
          logJson(msg)
        }
      }
      .init()

    LoadingHelper.setDefaultAdapterPool {
      register(ViewType.LOADING, LoadingAdapter())
    }
  }
}