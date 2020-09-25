package com.dylanc.dontforget.viewmodel.shared

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.data.repository.SettingRepository

class SharedViewModel @ViewModelInject constructor(
  private val settingRepository: SettingRepository
) : ViewModel() {
  val isShowNotification: LiveData<Boolean> = settingRepository.isShowNotification
  val isNightMode: LiveData<Boolean> = settingRepository.isNightMode

  fun showNotification(isShow: Boolean) {
    settingRepository.showNotification(isShow)
  }

  fun changeNightMode(isNightMode: Boolean) {
    settingRepository.changeNightMode(isNightMode)
  }
}