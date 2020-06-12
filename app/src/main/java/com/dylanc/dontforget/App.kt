@file:Suppress("unused")

package com.dylanc.dontforget

import android.app.Application
import com.dylanc.dontforget.adapter.loading.LoadingAdapter
import com.dylanc.dontforget.adapter.loading.ToolbarAdapter
import com.dylanc.dontforget.data.bean.ApiResponse
import com.dylanc.dontforget.data.net.AuthenticationException
import com.dylanc.dontforget.data.net.observeRequestSuccess
import com.dylanc.dontforget.data.net.persistentCookies
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.loadinghelper.ViewType
import com.dylanc.retrofit.helper.initRetrofit
import com.dylanc.utilktx.finishAllActivities
import com.dylanc.utilktx.logJson
import com.dylanc.utilktx.startActivity
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
      addHttpLoggingInterceptor { msg ->
        if (msg.startsWith("{") && msg.endsWith("}")) {
          logJson(msg)
        }
      }
    }

    LoadingHelper.setDefaultAdapterPool {
      register(ViewType.TITLE, ToolbarAdapter())
      register(ViewType.LOADING, LoadingAdapter())
    }

    observeRequestSuccess { response ->
      if (response is ApiResponse<*> && response.errorCode == -1001) {
        throw AuthenticationException(response.errorMsg)
      }
    }

    UpdateAppUtils.init(this)
  }
}