package com.dylanc.dontforget.ui.main.add_info

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.constant.KEY_INFO
import com.dylanc.dontforget.data.net.RxLoadingDialog
import com.dylanc.dontforget.databinding.MainActivityAddInfoBinding
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread
import com.dylanc.retrofit.helper.transformer.showLoading
import kotlinx.android.synthetic.main.layout_toolbar.*

class AddInfoActivity : AppCompatActivity() {

  private val viewModel: AddInfoViewModel by viewModels()
  private lateinit var binding: MainActivityAddInfoBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = setBindingContentView(R.layout.main_activity_add_info)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

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
        apiServiceOf<TodoApi>()
          .addTodo(viewModel.title.value!!, viewModel.content.value!!)
          .io2mainThread()
          .showLoading(RxLoadingDialog(this))
          .subscribe({ response ->
            val intent = Intent()
            intent.putExtra(KEY_INFO, response.data)
            setResult(RESULT_OK, intent)
            finish()
          }, {})
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

}
