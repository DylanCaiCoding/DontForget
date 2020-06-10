package com.dylanc.dontforget.view_model.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListStateViewModel : ViewModel() {
  val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(true)
}