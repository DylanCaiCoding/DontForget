package com.dylanc.dontforget.ui.main.add_info

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_ADD_INFO
import kotlinx.android.synthetic.main.common_layout_toolbar.toolbar
import kotlinx.android.synthetic.main.main_activity_add_info.*

class AddInfoActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity_add_info)

    toolbar.title = "添加信息"
    setSupportActionBar(toolbar)
    BarUtils.setStatusBarLightMode(this, true)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.text_complete, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      R.id.action_complete -> {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

}
