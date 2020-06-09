package com.dylanc.dontforget.ui.main.info

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.TitleConfig
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_INFO
import com.dylanc.dontforget.databinding.ActivityInfoBinding
import com.dylanc.dontforget.utils.setBindingContentView
import com.dylanc.dontforget.utils.setToolbar
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast
import kotlinx.coroutines.launch

class InsertInfoActivity : AppCompatActivity() {

  private val viewModel: InsertInfoViewModel by viewModels()
  private lateinit var binding: ActivityInfoBinding

  companion object {
    fun start(info: DontForgetInfo? = null) {
      startActivity<InsertInfoActivity>(KEY_INFO to info)
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
    viewModel.info.value = info
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
          lifecycleScope.launch {
            if (info == null) {
              viewModel.addInfo()
            } else {
              viewModel.updateInfo()
            }
//            finish()
          }
        }
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

}
