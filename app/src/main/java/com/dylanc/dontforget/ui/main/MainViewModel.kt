package com.dylanc.dontforget.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.repository.SettingRepository

class MainViewModel @ViewModelInject constructor(
  private val settingRepository: SettingRepository
) : ViewModel() {
  private var _isShowNotification = MutableLiveData(settingRepository.isShowNotification)
  val isShowNotification: LiveData<Boolean> get() = _isShowNotification

  private var _isNightMode = MutableLiveData(settingRepository.isNightMode)
  val isNightMode: LiveData<Boolean> get() = _isNightMode

  fun showNotification(isShow: Boolean) {
    settingRepository.isShowNotification = isShow
    _isShowNotification.value = isShow
  }

  fun changeNightMode(isNightMode: Boolean) {
    settingRepository.isNightMode = isNightMode
    this._isNightMode.value = isNightMode
  }
}