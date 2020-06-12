package com.dylanc.dontforget.data.net

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class RequestExceptionHandler : AbstractCoroutineContextElement(CoroutineExceptionHandler),
  CoroutineExceptionHandler {
  private val _requestException: MutableLiveData<RequestException> = MutableLiveData()
  val requestException: LiveData<RequestException> = _requestException

  override fun handleException(context: CoroutineContext, exception: Throwable) {
    val requestException = if (exception is RequestException) {
      exception
    } else {
      RequestException(exception)
    }
    _requestException.postValue(requestException)
  }
}

suspend fun <T : Any> request(
  requestCode: Any? = null,
  loadingType: Any? = null,
  block: suspend () -> T
): T =
  try {
    block().also { onRequestSuccessListener?.invoke(it) }
  } catch (e: Exception) {
    throw RequestException(e, e.message, requestCode, loadingType)
  }

private var onRequestSuccessListener: ((Any) -> Unit)? = null

fun observeRequestSuccess(onRequestSuccess: (Any) -> Unit) {
  onRequestSuccessListener = onRequestSuccess
}