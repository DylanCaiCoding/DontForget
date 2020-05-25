package com.dylanc.dontforget.ui.main.info

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.BarUtils
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_EDIT_MODE
import com.dylanc.dontforget.data.constant.KEY_INFO
import com.dylanc.dontforget.data.constant.REQUEST_CODE_UPDATE_INFO
import com.dylanc.dontforget.data.net.RxLoadingDialog
import com.dylanc.dontforget.databinding.ActivityInfoBinding
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.rxjava.io2mainThread
import com.dylanc.retrofit.helper.rxjava.showLoading
import com.dylanc.utilktx.*
import com.dylanc.utilktx.startActivityForResult
import kotlinx.android.synthetic.main.layout_toolbar.*

class InfoActivity : AppCompatActivity() {

  private val viewModel: InfoViewModel by viewModels()
  private lateinit var binding: ActivityInfoBinding

  companion object {
//    fun start(
//      activity: FragmentActivity,
//      info: DontForgetInfo? = null,
//      callback: (resultCode: Int, data: Intent?) -> Unit
//    ) {
//      if (info!=null){
//        activity.startActivityForResult<InfoActivity>(REQUEST_CODE_UPDATE_INFO, KEY_INFO to info, callback)
//      }
//    }
  }

  private var info: DontForgetInfo? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = setBindingContentView(R.layout.activity_info)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    info = intent.getParcelableExtra(KEY_INFO)
    if (info == null) {
      toolbar.title = "添加信息"
    } else {
      toolbar.title = "修改信息"
    }
    setSupportActionBar(toolbar)
    BarUtils.setStatusBarLightMode(this, true)

    viewModel.title.value = info?.title
    viewModel.content.value = info?.content

  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.text_complete, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      R.id.action_complete -> {
        if (viewModel.title.value == null) {
          toast("请输入标题")
        }
        if (info == null) {
          apiServiceOf<TodoApi>()
            .addTodo(viewModel.title.value!!, viewModel.content.value)
            .io2mainThread()
            .showLoading(RxLoadingDialog(this))
            .subscribe({ response ->
              val intent = Intent()
              intent.putExtra(KEY_INFO, response.data)
              setResult(RESULT_OK, intent)
              finish()
            }, {})
        } else {
          apiServiceOf<TodoApi>()
            .updateTodo(
              info!!.id,
              viewModel.title.value!!,
              viewModel.content.value,
              info!!.dateStr
            )
            .io2mainThread()
            .showLoading(RxLoadingDialog(this))
            .subscribe({ response ->
              val intent = Intent()
              intent.putExtra(KEY_INFO, response.data)
              intent.putExtra(KEY_EDIT_MODE, true)
              setResult(RESULT_OK, intent)
              finish()
            }, {})
        }
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

}
