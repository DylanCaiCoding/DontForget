package com.dylanc.dontforget.data.repository

import com.dylanc.utilktx.putSP
import com.dylanc.utilktx.spValueOf


const val KEY_SHOW_NOTIFICATION = "show_notification"
const val KEY_NIGHT_MODE = "night_mode"

object SettingRepository {
  var isShowNotification: Boolean
    get() = spValueOf(KEY_SHOW_NOTIFICATION, true)
    set(value) = putSP(KEY_SHOW_NOTIFICATION, value)

  var isNightMode: Boolean
    get() = spValueOf(KEY_NIGHT_MODE, false)
    set(value) = putSP(KEY_NIGHT_MODE, value)
}