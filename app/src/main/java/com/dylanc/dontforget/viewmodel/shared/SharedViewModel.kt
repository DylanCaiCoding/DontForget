package com.dylanc.dontforget.viewmodel.shared

import androidx.lifecycle.ViewModel
import com.dylanc.longan.EventLiveData

class SharedViewModel : ViewModel() {
  val showNotificationEvent = EventLiveData<Any>()
}