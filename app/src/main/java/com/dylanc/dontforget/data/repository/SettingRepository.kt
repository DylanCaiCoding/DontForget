package com.dylanc.dontforget.data.repository

import com.dylanc.utilktx.putSpValue
import com.dylanc.utilktx.spValueOf


class SettingRepository(
  localDataSource: SettingLocalDataSource
) {
  var isShowNotification = localDataSource.isShowNotification

  var isNightMode = localDataSource.isNightMode
}

class SettingLocalDataSource {

  companion object {
    const val KEY_SHOW_NOTIFICATION = "show_notification"
    const val KEY_NIGHT_MODE = "night_mode"
  }

  var isShowNotification: Boolean
    get() = spValueOf(KEY_SHOW_NOTIFICATION, true)
    set(value) = putSpValue(KEY_SHOW_NOTIFICATION, value)

  var isNightMode: Boolean
    get() = spValueOf(KEY_NIGHT_MODE, false)
    set(value) = putSpValue(KEY_NIGHT_MODE, value)
}