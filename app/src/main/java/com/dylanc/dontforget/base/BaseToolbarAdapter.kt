package com.dylanc.dontforget.base

import android.view.MenuItem
import android.view.View
import com.dylanc.loadinghelper.LoadingHelper

/**
 * @author Dylan Cai
 * @since 2019/11/17
 */
abstract class BaseToolbarAdapter<T : TitleConfig, VH : LoadingHelper.ViewHolder> :
  LoadingHelper.Adapter<VH>() {

  lateinit var config: T
}

open class TitleConfig(
  var titleText: String,
  var type: Type
) {
  var menuId = 0
    private set
  var onMenuItemClickListener: ((MenuItem) -> Boolean)? = null
    private set

  fun setMenu(menuId:Int,onMenuItemClickListener: (MenuItem) -> Boolean){
    this.menuId = menuId
    this.onMenuItemClickListener = onMenuItemClickListener
  }

  enum class Type {
    BACK, NO_BACK
  }
}
