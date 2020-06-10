package com.dylanc.dontforget.utils

import android.app.Activity
import android.view.MenuItem
import com.dylanc.dontforget.base.BaseToolbarAdapter
import com.dylanc.dontforget.base.TitleConfig
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.loadinghelper.ViewType


fun Activity.setToolbar(title: String, type: TitleConfig.Type = TitleConfig.Type.NO_BACK) =
  setToolbar(TitleConfig(title, type))

fun Activity.setToolbar(
  title: String, type: TitleConfig.Type = TitleConfig.Type.NO_BACK,
  menuId: Int, listener: (MenuItem) -> Boolean
) =
  setToolbar(TitleConfig(title, type).apply { setMenu(menuId, listener) })

fun Activity.setToolbar(config: TitleConfig): LoadingHelper =
  LoadingHelper(this).apply {
    val toolbarAdapter: BaseToolbarAdapter<TitleConfig, *> = getAdapter(ViewType.TITLE)
    toolbarAdapter.config = config
    setDecorHeader(ViewType.TITLE)
  }

fun Activity.loadingHelperOf(vararg adapters: Pair<Any, LoadingHelper.Adapter<*>>) =
  LoadingHelper(this).apply {
    for (adapter in adapters) {
      register(adapter.first, adapter.second)
    }
  }