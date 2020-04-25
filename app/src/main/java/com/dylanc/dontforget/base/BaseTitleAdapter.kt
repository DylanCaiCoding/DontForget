package com.dylanc.dontforget.base

import com.dylanc.loadinghelper.LoadingHelper

/**
 * @author Dylan Cai
 * @since 2019/11/17
 */
abstract class BaseTitleAdapter<T : TitleConfig, VH : LoadingHelper.ViewHolder> :
  LoadingHelper.Adapter<VH>() {
  lateinit var config: T
}