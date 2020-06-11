package com.dylanc.dontforget.data.net

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import com.dylanc.dontforget.data.bean.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * @author Dylan Cai
 */
@Suppress("RemoveExplicitTypeArguments")
suspend fun <T : Any> resource(block: suspend CoroutineScope.() -> T) =
  withContext(Dispatchers.IO) {
    try {
      val data = block()
      if (data is ApiResponse<*>) {
        when (data.errorCode) {
          0 -> Resource.success(data)
          else -> Resource.error<T>(data.errorMsg)
        }
      }
      Resource.success(data)
    } catch (e: Exception) {
      handlError<T>(e)
    }
  }

fun <T> resourceLiveData(block: suspend LiveDataScope<Resource<T>>.() -> Unit): LiveData<Resource<T>> =
  liveData {
    emit(Resource.loading())
    block()
  }

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
  companion object {
    fun <T> success(data: T? = null): Resource<T> {
      return Resource(Status.SUCCESS, data, null)
    }

    fun <T> error(msg: String, data: T? = null): Resource<T> {
      return Resource(Status.ERROR, data, msg)
    }

    fun <T> loading(data: T? = null): Resource<T> {
      return Resource(Status.LOADING, data, null)
    }
  }
}

fun <T> handlError(e: Exception): Resource<T> =
  when (e) {
    is HttpException -> error(getErrorMessage(e.code()))
    is SocketTimeoutException -> error(getErrorMessage(ErrorCodes.SOCKET_TIMEOUT))
    else -> if (e.message == null) {
      error(getErrorMessage(Int.MAX_VALUE))
    } else {
      error(e.message!!)
    }
  }

private fun getErrorMessage(code: Int): String =
  when (code) {
    ErrorCodes.SOCKET_TIMEOUT -> "Timeout"
    401 -> "Unauthorised"
    404 -> "Not found"
    else -> "Something went wrong"
  }

object ErrorCodes {
  const val SOCKET_TIMEOUT = 1000
}

enum class Status {
  SUCCESS,
  ERROR,
  LOADING
}