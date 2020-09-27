package com.dylanc.dontforget.base.event

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

typealias EventLiveData<T> = MutableLiveData<Event<T>>

@MainThread
fun <T> EventLiveData<T>.observeEvent(owner: LifecycleOwner, onChanged: (T) -> Unit) =
  observe(owner, {
    it.content?.let { content ->
      onChanged(content)
    }
  })

fun <T> EventLiveData<T>.postEventValue(value: T) =
  postValue(Event(value))

val <T> EventLiveData<T>.content get() = value?.content