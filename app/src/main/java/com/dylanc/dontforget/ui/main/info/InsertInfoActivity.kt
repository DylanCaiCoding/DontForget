package com.dylanc.dontforget.ui.main.info

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.TitleConfig
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_INFO
import com.dylanc.dontforget.data.constant.REQUEST_CODE_INSERT_INFO
import com.dylanc.dontforget.data.net.RxLoadingDialog
import com.dylanc.dontforget.databinding.ActivityInfoBinding
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.dontforget.utils.setToolbar
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.rxjava.autoDispose
import com.dylanc.retrofit.helper.rxjava.io2mainThread
import com.dylanc.retrofit.helper.rxjava.showLoading
import com.dylanc.utilktx.startActivityForResult
import com.dylanc.utilktx.toast

class InsertInfoActivity : AppCompatActivity() {

  private val viewModel: InsertInfoViewModel by viewModels()
  private lateinit var binding: ActivityInfoBinding

  companion object {
    fun start(
      activity: FragmentActivity,
      info: DontForgetInfo? = null,
      callback: (resultCode: Int, data: Intent?) -> Unit
    ) {
      activity.startActivityForResult<InsertInfoActivity>(
        REQUEST_CODE_INSERT_INFO,
        KEY_INFO to info,
        callback = callback
      )
    }
  }

  private var info: DontForgetInfo? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = setBindingContentView(R.layout.activity_info)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    info = intent.getParcelableExtra(KEY_INFO)
    viewModel.title.value = info?.title
    val title = if (info == null) {
      "添加信息"
    } else {
      "修改信息"
    }
    setToolbar(title, TitleConfig.Type.BACK, R.menu.text_complete, this::onOptionsItemSelected)
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      R.id.action_complete -> {
        if (TextUtils.isEmpty(viewModel.title.value)) {
          toast("请输入标题")
        } else {
          val info = info
          val api = if (info == null) {
            apiServiceOf<TodoApi>()
              .addTodo(viewModel.title.value!!)
          } else {
            apiServiceOf<TodoApi>()
              .updateTodo(info.id, viewModel.title.value!!, info.dateStr)
          }
          api.io2mainThread()
            .showLoading(RxLoadingDialog(this))
            .autoDispose(this)
            .subscribe({ response ->
              val intent = Intent()
              intent.putExtra(KEY_INFO, response.data)
              setResult(RESULT_OK, intent)
              finish()
            }, {})
        }
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

}
