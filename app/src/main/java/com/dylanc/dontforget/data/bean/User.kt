package com.dylanc.dontforget.data.bean

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Dylan Cai
 * @since 2020/4/16
 */
@Keep
@Entity(tableName = "user_table")
data class User(
  @PrimaryKey
  val id: Int,
  val admin: Boolean,
//  val chapterTops: List<Any>,
//  val collectIds: List<Int>,
  val email: String,
  val icon: String,
  val nickname: String,
  val password: String,
  val publicName: String,
  val token: String,
  val type: Int,
  val username: String
)