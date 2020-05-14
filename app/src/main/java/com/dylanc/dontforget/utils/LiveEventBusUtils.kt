package com.dylanc.dontforget.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.Observable

/**
 * @author Dylan Cai
 * @since 2020/5/14
 */
fun postEvent(key: String, value: Any) =
  LiveEventBus.get(key).post(value)

fun postEventAcrossApp(key: String, value: Any) =
  LiveEventBus.get(key).postAcrossApp(value)

fun postEventAcrossProcess(key: String, value: Any) =
  LiveEventBus.get(key).postAcrossProcess(value)

fun postEventDelay(owner: LifecycleOwner? = null, key: String, value: Any, delay: Long) =
  if (owner == null) {
    LiveEventBus.get(key).postDelay(value, delay)
  } else {
    LiveEventBus.get(key).postDelay(owner, value, delay)
  }

fun postEventAcrossOrderly(key: String, value: Any) =
  LiveEventBus.get(key).postOrderly(value)

inline fun <reified T> LifecycleOwner.observeEvent(key: String, crossinline observer: (T) -> Unit) =
  LiveEventBus.get(key, T::class.java).observe(this, Observer { observer(it) })

inline fun <reified T> LifecycleOwner.observeStickyEvent(
  key: String,
  crossinline observer: (T) -> Unit
) =
  LiveEventBus.get(key, T::class.java).observeSticky(this, Observer { observer(it) })

inline fun <reified T> observeForeverEvent(key: String, noinline observer: (T) -> Unit) =
  LiveEventBus.get(key, T::class.java).observeForever(observer)

inline fun <reified T> observeStickyForeverEvent(key: String, noinline observer: (T) -> Unit) =
  LiveEventBus.get(key, T::class.java).observeStickyForever(observer)

inline fun <reified T> cancelObserveEvent(key: String, noinline observer: (T) -> Unit) =
  LiveEventBus.get(key, T::class.java).removeObserver(observer)