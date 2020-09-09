package com.dylanc.dontforget.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListStateViewModel : ViewModel() {
  val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(true)
}