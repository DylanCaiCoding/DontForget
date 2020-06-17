package com.dylanc.dontforget.data.net


var onRequestSuccessListener: ((Any) -> Unit)? = null
  private set

object RequestSuccessHandler {
  fun observe(onRequestSuccess: (Any) -> Unit) {
    onRequestSuccessListener = onRequestSuccess
  }
}