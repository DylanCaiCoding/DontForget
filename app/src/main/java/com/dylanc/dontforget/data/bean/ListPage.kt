package com.dylanc.dontforget.data.bean

import com.google.gson.annotations.SerializedName

/**
 * @author Dylan Cai
 * @since 2020/4/25
 */
data class ListPage<T>(
  val curPage: Int,
  @SerializedName("datas") val list: List<T>,
  val offset: Int,
  val over: Boolean,
  val pageCount: Int,
  val size: Int,
  val total: Int
)