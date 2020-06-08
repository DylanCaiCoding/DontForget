package com.dylanc.dontforget.utils

import android.app.Activity
import android.view.MenuItem
import com.dylanc.dontforget.base.BaseTitleAdapter
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
    val titleAdapter: BaseTitleAdapter<TitleConfig, *> = getAdapter(ViewType.TITLE)
    titleAdapter.config = config
    setDecorHeader(ViewType.TITLE)
  }