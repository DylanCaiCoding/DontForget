package com.dylanc.dontforget.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.blankj.utilcode.util.BarUtils
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.api.VersionApi
import com.dylanc.dontforget.data.repository.UserRepository
import com.dylanc.dontforget.databinding.ActivityMainBinding
import com.dylanc.dontforget.ui.user.login.LoginActivity
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread
import com.dylanc.utilktx.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.toolbar
import update.UpdateAppUtils


class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()
  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = setBindingContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    BarUtils.setStatusBarLightMode(this, true)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    val navController = findNavController(R.id.nav_host_fragment)
    appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawer_layout)
    setupActionBarWithNavController(navController, appBarConfiguration)
//    nav_view.setupWithNavController(navController)

    nav_view.setNavigationItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.nav_check_update -> {
          apiServiceOf<VersionApi>()
            .check()
            .io2mainThread()
            .subscribe({ appVersion ->
              UpdateAppUtils
                .getInstance()
                .apkUrl(appVersion.installUrl)
                .updateTitle("检查到新版本 v${appVersion.versionShort}")
                .update()
            }, {})
          drawer_layout.closeDrawers()
        }
        else -> {
        }
      }
      false
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_logout -> {
        UserRepository.logout()
        startActivity<LoginActivity>()
        finish()
        true
      }
      else -> {
        super.onOptionsItemSelected(item)
      }
    }
  }
}
