package com.dylanc.dontforget.viewmodel.event

import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.base.event.EventLiveData

class SharedViewModel : ViewModel() {
  val isShowNotification = EventLiveData<Boolean>()
}