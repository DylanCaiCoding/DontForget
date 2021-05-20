package com.dylanc.dontforget.data.repository

import com.dylanc.longan.sharedPreferences


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

  var isShowNotification: Boolean by sharedPreferences(KEY_SHOW_NOTIFICATION ,true)

  var isNightMode: Boolean by sharedPreferences(KEY_NIGHT_MODE ,false)
}