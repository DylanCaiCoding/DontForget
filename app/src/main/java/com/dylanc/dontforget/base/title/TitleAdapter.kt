package com.dylanc.dontforget.base.title

import android.app.Activity
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.BaseTitleAdapter
import com.dylanc.dontforget.base.TitleConfig
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.utilktx.setStatusBarLightMode
import kotlinx.android.synthetic.main.layout_toolbar.view.*

/**
 * @author Dylan Cai
 * @since 2020/3/4
 */
class TitleAdapter : BaseTitleAdapter<TitleConfig, LoadingHelper.ViewHolder>() {

  override fun onCreateViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup
  ) = LoadingHelper.ViewHolder(
    inflater.inflate(R.layout.layout_toolbar, parent, false)
  )


  override fun onBindViewHolder(holder: LoadingHelper.ViewHolder) {
    val activity = holder.rootView.context as Activity
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//      activity.window.decorView.systemUiVisibility =
//        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//    }
    activity.setStatusBarLightMode(true)
    holder.rootView.apply {
      if (!TextUtils.isEmpty(config.titleText)) {
        toolbar.title = config.titleText
      }
      if (config.type === TitleConfig.Type.BACK) {
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { activity.finish() }
      } else {
        toolbar.navigationIcon = null
      }
      if (config.menuId > 0 && config.onMenuItemClickListener != null) {
        toolbar.inflateMenu(config.menuId)
        toolbar.setOnMenuItemClickListener { item: MenuItem ->
          config.onMenuItemClickListener?.invoke(item) ?: false
        }
      }
    }
  }
}