package com.dylanc.dontforget.data.net

/**
 * @author Dylan Cai
 */
@Suppress("RemoveExplicitTypeArguments")
suspend fun <T : Any> resource(block: suspend () -> T) =
  try {
    Resource.success(block())
  } catch (e: Exception) {
    Resource.error<T>(e.message ?: "Something went wrong")
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

enum class Status {
  SUCCESS,
  ERROR,
  LOADING
}