package com.dylanc.dontforget.base.event

class Event<T>(content: T) {
  private val _content: T = content
  var hasHandled = false
    private set
  val content: T?
    get() = if (hasHandled) {
      null
    } else {
      hasHandled = true
      _content
    }
}