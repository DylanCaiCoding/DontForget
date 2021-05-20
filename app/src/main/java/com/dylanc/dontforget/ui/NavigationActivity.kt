package com.dylanc.dontforget.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.dontforget.R
import com.dylanc.longan.isDarkMode
import com.dylanc.longan.isLightStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_navigation)
    isLightStatusBar = !isDarkMode
  }
}