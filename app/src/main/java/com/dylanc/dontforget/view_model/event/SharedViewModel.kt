package com.dylanc.dontforget.view_model.event

import androidx.lifecycle.ViewModel
import com.dylanc.dontforget.base.event.EventLiveData

class SharedViewModel : ViewModel() {
  val showNotification = EventLiveData<Boolean>()
}