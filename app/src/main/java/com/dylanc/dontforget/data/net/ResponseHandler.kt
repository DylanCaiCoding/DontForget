package com.dylanc.dontforget.data.net

import com.dylanc.dontforget.data.bean.ApiResponse

val responseHandler: (Any) -> Unit = { response ->
  if (response is ApiResponse<*>) {
    when (response.errorCode) {
      0 -> {
      }
      -1001 -> throw AuthenticationException(response.errorMsg)
      else -> throw RuntimeException(response.errorMsg)
    }
  }
}
