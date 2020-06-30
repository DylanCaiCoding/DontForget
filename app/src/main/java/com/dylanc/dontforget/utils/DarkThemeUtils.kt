package com.dylanc.dontforget.utils

import android.content.Context
import android.content.res.Configuration

fun Context.isDarkMode(): Boolean {
  val flag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
  return flag == Configuration.UI_MODE_NIGHT_YES
}