package com.dylanc.dontforget.data.bean

import java.io.Serializable

/**
 * @author Dylan Cai
 * @since 2020/1/23
 */
data class DontForgetInfo(
  val completeDate: Any,
  val completeDateStr: String,
  val content: String,
  val date: Long,
  val dateStr: String,
  val id: Int,
  val priority: Int,
  val status: Int,
  val title: String,
  val type: Int,
  val userId: Int
) : Serializable