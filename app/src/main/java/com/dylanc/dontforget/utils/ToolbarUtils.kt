@file:Suppress("unused")

package com.dylanc.dontforget.utils

import android.app.Activity
import android.view.MenuItem
import android.view.View
import com.dylanc.dontforget.adapter.loading.NavIconType
import com.dylanc.dontforget.adapter.loading.ToolbarAdapter
import com.dylanc.loadinghelper.LoadingHelper
import com.dylanc.loadinghelper.ViewType


fun Activity.setToolbar(
  title: String, type: NavIconType = NavIconType.NONE,
  menuId: Int = 0, listener: ((MenuItem) -> Boolean)? = null
) =
  setToolbar(ToolbarAdapter(title, type, menuId, listener))

fun Activity.setToolbar(adapter: LoadingHelper.Adapter<*>) =
  LoadingHelper(this).setToolbar(adapter)

fun LoadingHelper.setToolbar(
  title: String, type: NavIconType = NavIconType.NONE,
  menuId: Int = 0, listener: ((MenuItem) -> Boolean)? = null
) =
  setToolbar(ToolbarAdapter(title, type, menuId, listener))

fun LoadingHelper.setToolbar(adapter: LoadingHelper.Adapter<*>) = apply {
  register(ViewType.TITLE, adapter)
  setDecorHeader(ViewType.TITLE)
}

fun View.setToolbar(
  title: String, type: NavIconType = NavIconType.NONE,
  menuId: Int = 0, listener: ((MenuItem) -> Boolean)? = null
) =
  setToolbar(ToolbarAdapter(title, type, menuId, listener))

fun View.setToolbar(adapter: LoadingHelper.Adapter<*>) =
  LoadingHelper(this).apply {
    register(ViewType.TITLE, adapter)
    setDecorHeader(ViewType.TITLE)
  }

fun Activity.loadingHelperOf(vararg adapters: Pair<Any, LoadingHelper.Adapter<*>>) =
  LoadingHelper(this).apply {
    for (adapter in adapters) {
      register(adapter.first, adapter.second)
    }
  }