package com.dylanc.dontforget.data.bean

/**
 * @author Dylan Cai
 * @since 2020/4/16
 */
data class ApiResponse<T>(
  val data: T,
  val errorCode: Int,
  val errorMsg: String
)