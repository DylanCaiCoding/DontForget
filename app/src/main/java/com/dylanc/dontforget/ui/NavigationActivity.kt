package com.dylanc.dontforget.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.dontforget.R
import com.dylanc.dontforget.utils.isDarkMode
import com.dylanc.utilktx.isStatusBarLightMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_navigation)
    isStatusBarLightMode = !isDarkMode
  }
}