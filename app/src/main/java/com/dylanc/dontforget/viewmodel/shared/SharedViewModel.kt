package com.dylanc.dontforget.viewmodel.shared

import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData

class SharedViewModel : ViewModel() {
  val showNotificationEvent = UnPeekLiveData<Any>()
}