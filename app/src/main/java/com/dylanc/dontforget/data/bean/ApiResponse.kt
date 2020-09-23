package com.dylanc.dontforget.data.bean

import androidx.annotation.Keep
import com.dylanc.dontforget.data.net.AuthenticationException

/**
 * @author Dylan Cai
 * @since 2020/4/16
 */
@Suppress("UNCHECKED_CAST")
fun <T> ApiResponse<T>.parseData(): T =
  when (errorCode) {
    0 -> data ?: Any() as T
    -1001 -> throw AuthenticationException(errorMsg)
    else -> throw RuntimeException(errorMsg)
  }

@Keep
data class ApiResponse<T>(
  val data: T?,
  val errorCode: Int,
  val errorMsg: String
)