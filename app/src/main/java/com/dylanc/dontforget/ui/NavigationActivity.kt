package com.dylanc.dontforget.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.dylanc.dontforget.R
import com.dylanc.dontforget.utils.isDarkMode
import com.dylanc.utilktx.setStatusBarLightMode
import kotlinx.android.synthetic.main.activity_main.*

class NavigationActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_navigation)
    setStatusBarLightMode(!isDarkMode())
  }
}