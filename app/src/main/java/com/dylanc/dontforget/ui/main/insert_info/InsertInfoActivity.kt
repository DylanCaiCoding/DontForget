package com.dylanc.dontforget.ui.main.insert_info

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.TitleConfig
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_INFO
import com.dylanc.dontforget.data.net.LoadingDialog
import com.dylanc.dontforget.data.net.Status
import com.dylanc.dontforget.databinding.ActivityInfoBinding
import com.dylanc.dontforget.utils.bindContentView
import com.dylanc.dontforget.utils.lifecycleOwner
import com.dylanc.dontforget.utils.setToolbar
import com.dylanc.dontforget.view_model.request.InfoRequestViewModel
import com.dylanc.utilktx.startActivity
import com.dylanc.utilktx.toast

class InsertInfoActivity : AppCompatActivity() {

  private val viewModel: InsertInfoViewModel by viewModels()
  private val requestViewModel: InfoRequestViewModel by viewModels()
  private lateinit var binding: ActivityInfoBinding
  private val loadingDialog = LoadingDialog()
  private val clickProxy = ClickProxy()

  companion object {
    fun start(info: DontForgetInfo? = null) {
      startActivity<InsertInfoActivity>(KEY_INFO to info)
    }
  }

  private var info: DontForgetInfo? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = bindContentView(R.layout.activity_info)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    info = intent.getParcelableExtra(KEY_INFO)
    viewModel.title.value = info?.title
    setToolbar(
      toolbarTitle, TitleConfig.Type.BACK,
      R.menu.text_complete, clickProxy::onOptionsItemSelected
    )
  }

  private val toolbarTitle: String
    get() = if (info == null) {
      "添加信息"
    } else {
      "修改信息"
    }

  inner class ClickProxy {

    fun onOptionsItemSelected(item: MenuItem) =
      when (item.itemId) {
        R.id.action_complete -> {
          if (TextUtils.isEmpty(viewModel.title.value)) {
            toast("请输入标题")
          } else {
            val resource = if (info == null) {
              requestViewModel.addInfo(viewModel.title.value!!)
            } else {
              requestViewModel.updateInfo(info!!.id, viewModel.title.value!!, info!!.dateStr)
            }
            resource.observe(lifecycleOwner) {
              when (it.status) {
                Status.LOADING -> loadingDialog.show(supportFragmentManager)
                Status.SUCCESS -> {
                  loadingDialog.dismiss()
                  finish()
                }
                Status.ERROR -> {
                  loadingDialog.dismiss()
                  toast(it.message)
                }
              }
            }
          }
          true
        }
        else -> false
      }
  }

}
