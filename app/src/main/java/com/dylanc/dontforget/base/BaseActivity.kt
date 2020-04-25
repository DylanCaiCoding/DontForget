package com.dylanc.dontforget.base

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.loadinghelper.ViewType
import com.dylanc.dontforget.base.title.TitleAdapter

/**
 * @author Dylan Cai
 * @since 2019/11/16
 */
abstract class BaseActivity : AppCompatActivity(), LoadingHelper.OnReloadListener {
  companion object {
    @JvmStatic
    var defaultContentAdapter: LoadingHelper.ContentAdapter<*>? = null
  }

  lateinit var loadingHelper: LoadingHelper
    private set

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initContentView()
    loadingHelper.register(ViewType.TITLE,TitleAdapter())
    initData(savedInstanceState)
    initViews()
  }

  abstract fun initContentView()

  abstract fun initData(savedInstanceState: Bundle?)

  abstract fun initViews()

  @JvmOverloads
  fun setContentView(
    @LayoutRes layoutResID: Int,
    @IdRes contentViewId: Int = android.R.id.content,
    contentAdapter: LoadingHelper.ContentAdapter<*>? = defaultContentAdapter
  ) {
    super.setContentView(layoutResID)
    loadingHelper = LoadingHelper(findViewById<View>(contentViewId), contentAdapter)
    loadingHelper.setOnReloadListener(this)
  }

  @JvmOverloads
  fun addTitleView(title: String, type: TitleConfig.Type = TitleConfig.Type.NO_BACK) =
    addTitleView(TitleConfig(title, type))

  @JvmOverloads
  fun addTitleView(
    title: String, type: TitleConfig.Type = TitleConfig.Type.NO_BACK,
    rightIcon: Int, rightBtnClickListener: () -> Unit
  ) =
    addTitleView(TitleConfig(title, type).apply { setRightBtn(rightIcon, rightBtnClickListener) })

  @JvmOverloads
  fun addTitleView(
    title: String, type: TitleConfig.Type = TitleConfig.Type.NO_BACK,
    rightText: String, rightBtnClickListener: () -> Unit
  ) =
    addTitleView(TitleConfig(title, type).apply { setRightBtn(rightText, rightBtnClickListener) })

  private fun addTitleView(config: TitleConfig) {
    val titleAdapter: BaseTitleAdapter<TitleConfig, *> = loadingHelper.getAdapter(ViewType.TITLE)
    titleAdapter.config = config
//    loadingHelper.addTitleView()
  }

//  fun addHeaderView(viewType: Any)  = loadingHelper.addHeaderView(viewType)

  fun showLoadingView() = loadingHelper.showLoadingView()

  fun showContentView() = loadingHelper.showContentView()

  fun showErrorView() = loadingHelper.showErrorView()

  fun showEmptyView() = loadingHelper.showEmptyView()

  fun showCustomView(viewType: Any) = loadingHelper.showView(viewType)

  override fun onReload() {}
}