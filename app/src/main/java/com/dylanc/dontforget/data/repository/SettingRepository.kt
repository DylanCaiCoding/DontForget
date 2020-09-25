package com.dylanc.dontforget.data.repository

import androidx.lifecycle.MutableLiveData
import com.dylanc.utilktx.putSpValue
import com.dylanc.utilktx.spValueOf


const val KEY_SHOW_NOTIFICATION = "show_notification"
const val KEY_NIGHT_MODE = "night_mode"

class SettingRepository(
  private val localDataSource: SettingLocalDataSource
) {
  var isShowNotification = MutableLiveData(localDataSource.isShowNotification)

  var isNightMode = MutableLiveData(localDataSource.isNightMode)

  fun showNotification(isShow: Boolean) {
    localDataSource.isShowNotification = isShow
    isShowNotification.value = isShow
  }

  fun changeNightMode(isNightMode: Boolean) {
    localDataSource.isNightMode = isNightMode
    this.isNightMode.value = isNightMode
  }
}

class SettingLocalDataSource {
  var isShowNotification: Boolean
    get() = spValueOf(KEY_SHOW_NOTIFICATION, true)
    set(value) = putSpValue(KEY_SHOW_NOTIFICATION, value)

  var isNightMode: Boolean
    get() = spValueOf(KEY_NIGHT_MODE, false)
    set(value) = putSpValue(KEY_NIGHT_MODE, value)
}