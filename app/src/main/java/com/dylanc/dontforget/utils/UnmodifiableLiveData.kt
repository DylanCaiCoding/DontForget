@file:Suppress("unused")

package com.dylanc.dontforget.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> MutableLiveData<T>.toLiveData() :LiveData<T> = UnmodifiableLiveData(this)

class UnmodifiableLiveData<T>(
  private val mutableLiveData: MutableLiveData<T>
) : LiveData<T>() {

  override fun observe(owner: LifecycleOwner, observer: Observer<in T>) =
    mutableLiveData.observe(owner, observer)

  override fun observeForever(observer: Observer<in T>) = mutableLiveData.observeForever(observer)

  override fun hasActiveObservers() = mutableLiveData.hasActiveObservers()

  override fun hasObservers() = mutableLiveData.hasObservers()

  override fun removeObserver(observer: Observer<in T>) = mutableLiveData.removeObserver(observer)

  override fun removeObservers(owner: LifecycleOwner) = mutableLiveData.removeObservers(owner)

  override fun getValue() = mutableLiveData.value
}