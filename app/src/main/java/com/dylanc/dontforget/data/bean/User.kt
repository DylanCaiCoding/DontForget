package com.dylanc.dontforget.data.bean

/**
 * @author Dylan Cai
 * @since 2020/4/16
 */
data class User(
  val admin: Boolean,
  val chapterTops: List<Any>,
  val collectIds: List<Int>,
  val email: String,
  val icon: String,
  val id: Int,
  val nickname: String,
  val password: String,
  val publicName: String,
  val token: String,
  val type: Int,
  val username: String
)